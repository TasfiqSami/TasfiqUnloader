package be.wwx.tasfiqunloader;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
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

    private static final Duration TIMEOUT = Duration.ofSeconds(10);

    private BlacklistLoader() {
    }

    static Set<String> load(List<String> entries, Logger logger) {
        Set<String> names = new HashSet<>();
        HttpClient client = null;

        for (String entry : entries) {
            String trimmed = entry.trim();
            if (trimmed.isEmpty()) {
                continue;
            }
            if (!isUrl(trimmed)) {
                names.add(trimmed.toLowerCase(Locale.ROOT));
                continue;
            }
            if (client == null) {
                client = HttpClient.newBuilder()
                        .connectTimeout(TIMEOUT)
                        .followRedirects(HttpClient.Redirect.NORMAL)
                        .build();
            }
            try {
                fetch(client, trimmed, names);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                logger.severe("Interrupted while getting blacklist from URL: " + trimmed);
                return names;
            } catch (IOException | IllegalArgumentException e) {
                logger.severe("Failed to get blacklist from URL: " + trimmed + " (" + e.getMessage() + ")");
            }
        }
        return names;
    }

    private static boolean isUrl(String entry) {
        String lower = entry.toLowerCase(Locale.ROOT);
        return lower.startsWith("http://") || lower.startsWith("https://");
    }

    private static void fetch(HttpClient client, String url, Set<String> into)
            throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder(URI.create(url)).timeout(TIMEOUT).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() / 100 != 2) {
            throw new IOException("HTTP " + response.statusCode());
        }
        for (String line : response.body().split("\\R")) {
            String name = line.trim();
            if (!name.isEmpty() && !name.startsWith("#")) {
                into.add(name.toLowerCase(Locale.ROOT));
            }
        }
    }
}
