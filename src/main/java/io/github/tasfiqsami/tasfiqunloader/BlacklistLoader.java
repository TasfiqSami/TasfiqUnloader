package io.github.tasfiqsami.tasfiqunloader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.logging.Logger;

/**
 * Resolves the blacklist entries from config.yml into a set of lower-case plugin names.
 * Entries starting with http:// or https:// are downloaded and read as one plugin name per line.
 *
 * <p>This blocks on network I/O, so never call it from the main server thread.
 */
final class BlacklistLoader {

    private static final int TIMEOUT_MILLIS = 10_000;

    private BlacklistLoader() {
    }

    static Set<String> load(List<String> entries, Logger logger) {
        Set<String> names = new HashSet<String>();

        for (String entry : entries) {
            String trimmed = entry.trim();
            if (trimmed.isEmpty()) {
                continue;
            }
            if (!isUrl(trimmed)) {
                names.add(trimmed.toLowerCase(Locale.ROOT));
                continue;
            }
            try {
                fetch(trimmed, names);
            } catch (IOException e) {
                logger.severe("Failed to get blacklist from URL: " + trimmed + " (" + e.getMessage() + ")");
            }
        }
        return names;
    }

    private static boolean isUrl(String entry) {
        String lower = entry.toLowerCase(Locale.ROOT);
        return lower.startsWith("http://") || lower.startsWith("https://");
    }

    private static void fetch(String url, Set<String> into) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setConnectTimeout(TIMEOUT_MILLIS);
        connection.setReadTimeout(TIMEOUT_MILLIS);
        connection.setInstanceFollowRedirects(true);
        try {
            int status = connection.getResponseCode();
            if (status / 100 != 2) {
                throw new IOException("HTTP " + status);
            }
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String name = line.trim();
                    if (!name.isEmpty() && !name.startsWith("#")) {
                        into.add(name.toLowerCase(Locale.ROOT));
                    }
                }
            }
        } finally {
            connection.disconnect();
        }
    }
}
