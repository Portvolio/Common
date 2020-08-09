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
    private boolean started = false;

    public StartScenarioCommand(JavaPlugin plugin, Consumer<BukkitTask> runnable, int ticks)
    {
        this.plugin = plugin;
        this.runnable = runnable;
        this.ticks = ticks;
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

    public void register()
    {
        plugin.getCommand("startscenario").setExecutor(this);
    }

}
