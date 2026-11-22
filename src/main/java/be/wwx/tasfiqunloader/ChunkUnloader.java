package be.wwx.tasfiqunloader;

import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;

/**
 * Unloads loaded chunks (saving them first). Must be called from the main server thread.
 */
final class ChunkUnloader {

    private static final long MB = 1024L * 1024L;

    private final Logger logger;

    ChunkUnloader(Logger logger) {
        this.logger = logger;
    }

    void unloadAll(boolean runGc) {
        int unloaded = 0;
        for (World world : Bukkit.getWorlds()) {
            for (Chunk chunk : world.getLoadedChunks()) {
                // Returns false for chunks that must stay loaded (force-loaded, plugin tickets, ...).
                if (chunk.unload(true)) {
                    unloaded++;
                }
            }
        }

        if (unloaded > 0) {
            logger.info("Unloaded " + unloaded + " chunks");
            if (runGc) {
                collectGarbage();
            }
        }
    }

    private void collectGarbage() {
        Runtime runtime = Runtime.getRuntime();
        long before = runtime.freeMemory();
        runtime.gc();
        long freedMb = (runtime.freeMemory() - before) / MB;
        if (freedMb > 0) {
            logger.info(freedMb + " MB memory freed using Java garbage collector");
        }
    }
}
