package be.wwx.tasfiqunloader;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.IllegalPluginAccessException;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

/**
 * Freezes the server while nobody is online and unloads chunks to save CPU and memory.
 */
public final class TasfiqUnloader extends JavaPlugin {

    private Settings settings;
    private ChunkUnloader chunkUnloader;

    /** Runtime on/off switch, flipped by /hibernate. Starts from config.yml. */
    private boolean active;
    /** True when no blacklisted plugin is installed and the hibernation loop is running. */
    private boolean hibernating;
    /** True once worlds were saved for the current empty period. */
    private boolean worldsSaved;
    /** Incremented on every (re)start so stale async blacklist results are ignored. */
    private int generation;

    private BukkitTask task;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        chunkUnloader = new ChunkUnloader(getLogger());

        PluginCommand command = getCommand("hibernate");
        if (command != null) {
            HibernateCommand handler = new HibernateCommand(this);
            command.setExecutor(handler);
            command.setTabCompleter(handler);
        }

        loadSettings();
        // Wait one tick so every other plugin is enabled before the blacklist is checked.
        getServer().getScheduler().runTask(this, this::start);
    }

    @Override
    public void onDisable() {
        stopTask();
        generation++;
    }

    // ----- state used by the command -----

    boolean isActive() {
        return active;
    }

    boolean toggleActive() {
        active = !active;
        worldsSaved = false;
        return active;
    }

    boolean isSleeping() {
        return active && hibernating && getServer().getOnlinePlayers().isEmpty();
    }

    void reload() {
        reloadConfig();
        loadSettings();
        start();
    }

    // ----- lifecycle -----

    private void loadSettings() {
        settings = Settings.from(getConfig(), getLogger());
        active = settings.enabled();
    }

    private void start() {
        stopTask();
        hibernating = false;
        worldsSaved = false;

        final int current = ++generation;
        final Settings snapshot = settings;

        // The blacklist may point at a URL, so resolve it off the main thread.
        getServer().getScheduler().runTaskAsynchronously(this, () -> {
            Set<String> blacklist = BlacklistLoader.load(snapshot.blacklist(), getLogger());
            try {
                getServer().getScheduler().runTask(this, () -> {
                    if (current == generation) {
                        begin(blacklist);
                    }
                });
            } catch (IllegalPluginAccessException ignored) {
                // Plugin was disabled while the blacklist was downloading.
            }
        });
    }

    private void begin(Set<String> blacklist) {
        List<String> conflicts = Arrays.stream(getServer().getPluginManager().getPlugins())
                .map(Plugin::getName)
                .filter(name -> blacklist.contains(name.toLowerCase(Locale.ROOT)))
                .sorted()
                .toList();

        if (conflicts.isEmpty()) {
            hibernating = true;
            task = getServer().getScheduler().runTaskTimer(this, this::hibernateTick, 0L, 1L);
            return;
        }

        getLogger().warning("Blacklisted plugins were found (plugins that will stop working if TasfiqUnloader "
                + "hibernates the server). Hibernation is disabled, but chunks will still be unloaded. "
                + "Edit the blacklist in config.yml if you want to force-enable hibernation.");
        getLogger().warning("Blacklisted plugins: " + String.join(", ", conflicts));

        if (settings.unloadChunks()) {
            long period = settings.unloadIntervalSeconds() * 20L;
            task = getServer().getScheduler().runTaskTimer(this, this::unloadOnlyTick, period, period);
        }
    }

    private void stopTask() {
        if (task != null) {
            task.cancel();
            task = null;
        }
    }

    // ----- scheduled work (main thread) -----

    private void hibernateTick() {
        if (!active || !getServer().getOnlinePlayers().isEmpty()) {
            worldsSaved = false;
            return;
        }

        if (!worldsSaved) {
            getLogger().info("Saving worlds before hibernation");
            for (World world : Bukkit.getWorlds()) {
                world.save();
            }
            getLogger().info("Worlds saved!");
            worldsSaved = true;
            return;
        }

        // Sleeping on the main thread is what freezes the server.
        try {
            Thread.sleep(settings.sleepMillis());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return;
        }

        if (settings.unloadChunks()) {
            chunkUnloader.unloadAll(settings.gcAfterUnload());
        }
    }

    private void unloadOnlyTick() {
        if (active && getServer().getOnlinePlayers().isEmpty()) {
            chunkUnloader.unloadAll(settings.gcAfterUnload());
        }
    }
}
