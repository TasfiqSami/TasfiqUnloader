package io.github.tasfiqsami.tasfiqunloader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.configuration.file.FileConfiguration;

/**
 * Immutable snapshot of config.yml.
 */
final class Settings {

    private static final long MIN_SLEEP_MILLIS = 1L;
    private static final long MAX_SLEEP_MILLIS = 10_000L;

    private final boolean enabled;
    private final boolean unloadChunks;
    private final boolean gcAfterUnload;
    private final long sleepMillis;
    private final int unloadIntervalSeconds;
    private final List<String> blacklist;

    private Settings(boolean enabled, boolean unloadChunks, boolean gcAfterUnload, long sleepMillis,
            int unloadIntervalSeconds, List<String> blacklist) {
        this.enabled = enabled;
        this.unloadChunks = unloadChunks;
        this.gcAfterUnload = gcAfterUnload;
        this.sleepMillis = sleepMillis;
        this.unloadIntervalSeconds = unloadIntervalSeconds;
        this.blacklist = blacklist;
    }

    static Settings from(FileConfiguration config, Logger logger) {
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
                Collections.unmodifiableList(new ArrayList<String>(config.getStringList("blacklist"))));
    }

    boolean enabled() {
        return enabled;
    }

    boolean unloadChunks() {
        return unloadChunks;
    }

    boolean gcAfterUnload() {
        return gcAfterUnload;
    }

    long sleepMillis() {
        return sleepMillis;
    }

    int unloadIntervalSeconds() {
        return unloadIntervalSeconds;
    }

    List<String> blacklist() {
        return blacklist;
    }
}
