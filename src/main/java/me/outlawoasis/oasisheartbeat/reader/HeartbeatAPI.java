
package me.outlawoasis.oasisheartbeat.reader;

import me.outlawoasis.oasisheartbeat.model.HeartbeatModel;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class HeartbeatAPI {

    // In-memory cache of all known servers
    private static final Map<String, HeartbeatModel> CACHE = new ConcurrentHashMap<>();

    // Get model for a server (null = unknown)
    public static HeartbeatModel get(String server) {
        return CACHE.get(server);
    }

    // Set/update model for a server
    public static void set(String server, HeartbeatModel model) {
        CACHE.put(server, model);
    }

    // Remove server entirely (used when Redis key disappears)
    public static void remove(String server) {
        CACHE.remove(server);
    }

    // Get all known servers (used for cleanup)
    public static Set<String> getAllServers() {
        return CACHE.keySet();
    }

    // Mark server offline without deleting it
    public static void markOffline(String server) {
        HeartbeatModel model = CACHE.get(server);
        if (model != null) {
            model.setStatus("offline");
            CACHE.put(server, model);
        }
    }
}
