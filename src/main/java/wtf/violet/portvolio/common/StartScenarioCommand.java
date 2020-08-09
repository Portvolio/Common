package wtf.violet.portvolio.common;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.function.Consumer;

/**
 * A command that starts a task, common for "every x amount of time" UHC scenarios
 */
public class StartScenarioCommand implements CommandExecutor
{

    private final JavaPlugin plugin;
    private final Consumer<BukkitTask> runnable;
    private final int ticks;
    private final boolean sync;
    private boolean started = false;

    /**
     * Initialize the command, with a sync task.
     * @param plugin Plugin to base itself on
     * @param runnable Runnable to execute
     * @param ticks Ticks for the delay and period
     */
    public StartScenarioCommand(
        final JavaPlugin plugin,
        final Consumer<BukkitTask> runnable,
        final int ticks
    )
    {
        this(plugin, runnable, ticks, true);
    }

    /**
     * Initialize the command.
     * @param plugin Plugin to base itself on
     * @param runnable Runnable to execute
     * @param ticks Ticks for the delay and period
     * @param sync Whether it should be run synchronously
     */
    public StartScenarioCommand(
        final JavaPlugin plugin,
        final Consumer<BukkitTask> runnable,
        final int ticks,
        final boolean sync
    )
    {
        this.plugin = plugin;
        this.runnable = runnable;
        this.ticks = ticks;
        this.sync = sync;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (started)
        {
            sender.sendMessage(ChatColor.RED + "Scenario is already started!");
            return true;
        }

        started = true;

        plugin.getServer().getScheduler().runTaskTimer(plugin, runnable, ticks, ticks);
        sender.sendMessage(ChatColor.GREEN + "The scenario has been started!");

        return true;
    }

    /**
     * Register the command to /startscenario.
     */
    public void register()
    {
        plugin.getCommand("startscenario").setExecutor(this);
    }

}
