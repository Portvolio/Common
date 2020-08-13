package wtf.violet.common.command;

import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnull;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public abstract class BaseCommand<PluginT extends JavaPlugin> extends Command
{

    @Getter
    private final PluginT plugin;

    /**
     * Initialize a command.
     * @param plugin Plugin
     * @param description Description of the command
     * @param usage Usage of the command, after /commandName
     * @param permission Permission, null if none
     * @param aliases Aliases and name of the command
     */
    public BaseCommand(
        final PluginT plugin,
        final String description,
        final String usage,
        final String permission,
        final String... aliases
    )
    {
        super(aliases[0], description, "/" + aliases[0] + " " + usage, (
            // This is stupid, but super has to be the first call
            aliases.length == 1
                ? Collections.emptyList()
                : Arrays.asList(Arrays.copyOfRange(aliases, 1, aliases.length))
        ));

        this.plugin = plugin;

        if (permission != null)
        {
            setPermission(permission);
        }
    }

    /** Register the command */
    public void register()
        throws
        NoSuchMethodException,
        InvocationTargetException,
        IllegalAccessException
    {
        final Class<?> serverClass = getPlugin().getServer().getClass();

        final SimpleCommandMap commandMap = (SimpleCommandMap) serverClass
            .getDeclaredMethod("getCommandMap")
            .invoke(plugin.getServer());

        commandMap.register(getName(), plugin.getName().toLowerCase(), this);
    }

    /** Log exceptions instead of throwing them */
    public void sneakyRegister()
    {
        try
        {
            register();
        } catch (final Throwable rock)
        {
            rock.printStackTrace();
        }
    }

    /**
     * Executor. Do not extend this, extend run() instead.
     * @param sender Command sender
     * @param commandLabel Label
     * @param args Args
     * @return Success
     * @deprecated
     */
    @Override
    public boolean execute(
        @Nonnull final CommandSender sender,
        @Nonnull final String commandLabel,
        @Nonnull final String[] args
    )
    {
        final String perm = getPermission();

        if (perm != null && !sender.hasPermission(perm))
        {
            error(sender, "You do not have permission to do this!");
            return true;
        }

        run(plugin, commandLabel, sender, args);
        return true;
    }

    /**
     * Run the command, not yet implemented.
     * @param plugin Plugin
     * @param sender The command sender
     * @param args Arguments
     */
    public void run(
        final PluginT plugin,
        final String alias,
        final CommandSender sender,
        final String[] args
    )
    {}

    /**
     * Util method to require a player, sends an error otherwise.
     * @param sender The command sender
     * @return Player if a player is able to be extracted, otherwise null
     */
    protected static Player requirePlayer(final CommandSender sender)
    {
        if (sender instanceof Player)
        {
            return (Player) sender;
        }

        error(sender, "You must be a player to run this command.");

        return null;
    }

    /**
     * Util method to send an error to a player.
     * @param sender Sender
     * @param message Message
     */
    protected static void error(final CommandSender sender, final String message)
    {
        sender.sendMessage(ChatColor.RED + message);
    }

    /**
     * Util method to send a success to a player.
     * @param sender Sender
     * @param message Message
     */
    protected static void success(final CommandSender sender, final String message)
    {
        sender.sendMessage(ChatColor.GREEN + message);
    }


    /** Default tab complete -- do not use */
    @SuppressWarnings("NullableProblems")
    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args)
        throws IllegalArgumentException
    {
        // So to not have the nullable warnings
        return tab(plugin, sender, alias, args);
    }

    /**
     * Simpler tab complete -- extend me.
     * @param sender CommandSender
     * @param args Args
     * @return Completions
     */
    public List<String> tab(
        final PluginT plugin,
        final CommandSender sender,
        final String alias,
        final String[] args
    )
    {
        return null;
    }

}
