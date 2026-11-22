package be.wwx.tasfiqunloader;

import java.util.List;
import java.util.logging.Logger;

import org.bukkit.configuration.file.FileConfiguration;

/**
 * Immutable snapshot of config.yml.
 */
public record Settings(
        boolean enabled,
        boolean unloadChunks,
        boolean gcAfterUnload,
        long sleepMillis,
        int unloadIntervalSeconds,
        List<String> blacklist) {

    private static final long MIN_SLEEP_MILLIS = 1L;
    private static final long MAX_SLEEP_MILLIS = 10_000L;

    public static Settings from(FileConfiguration config, Logger logger) {
        long sleep = config.getLong("sleepMillis", 1000L);
        long clamped = Math.max(MIN_SLEEP_MILLIS, Math.min(MAX_SLEEP_MILLIS, sleep));
        if (clamped != sleep) {
            logger.warning("sleepMillis must be between " + MIN_SLEEP_MILLIS + " and " + MAX_SLEEP_MILLIS
                    + ", using " + clamped + " instead of " + sleep);
        }

        int interval = Math.max(1, config.getInt("unloadIntervalSeconds", 30));

        return new Settings(
                config.getBoolean("enabled", true),
                config.getBoolean("unloadChunks", true),
                config.getBoolean("gcAfterUnload", true),
                clamped,
                interval,
                List.copyOf(config.getStringList("blacklist")));
    }
}
