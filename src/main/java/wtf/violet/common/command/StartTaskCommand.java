package wtf.violet.common.command;

import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

import java.util.function.Consumer;

/**
 * A command that starts a task, common for "every x amount of time" UHC scenarios
 */
public class StartTaskCommand<PluginT extends JavaPlugin> extends BaseCommand<PluginT>
{

    private final Consumer<BukkitTask> runnable;
    private final int ticks;
    private final boolean sync;
    private boolean started = false;

    /**
     * Initialize the command, with a sync task. Assuming commandLabel is startscenario
     * @param plugin Plugin to base itself on
     * @param runnable Runnable to execute
     * @param ticks Ticks for the delay and period
     */
    public StartTaskCommand(
        final PluginT plugin,
        final Consumer<BukkitTask> runnable,
        final int ticks
    )
    {
        this(plugin, runnable, ticks, true, "startscenario");
    }

    /**
     * Initialize the command.
     * @param plugin Plugin to base itself on
     * @param runnable Runnable to execute
     * @param ticks Ticks for the delay and period
     * @param sync Whether it should be run synchronously
     */
    public StartTaskCommand(
        final PluginT plugin,
        final Consumer<BukkitTask> runnable,
        final int ticks,
        final boolean sync,
        final String commandLabel
    )
    {
        super(plugin, "Start the scenario", "", "scenario.start", commandLabel);

        this.runnable = runnable;
        this.ticks = ticks;
        this.sync = sync;
    }

    @Override
    public void run(PluginT plugin, String alias, CommandSender sender, String[] args)
    {
        if (started)
        {
            error(sender, "Scenario is already started!");
            return;
        }

        started = true;

        final BukkitScheduler scheduler = plugin.getServer().getScheduler();

        if (sync)
        {
            scheduler.runTaskTimer(plugin, runnable, ticks, ticks);
        } else
        {
            scheduler.runTaskTimerAsynchronously(plugin, runnable, ticks, ticks);
        }

        success(sender, "The scenario has been started!");
    }

}
