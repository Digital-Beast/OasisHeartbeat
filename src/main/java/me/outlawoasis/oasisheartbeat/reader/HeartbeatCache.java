package me.outlawoasis.oasisheartbeat.reader;

import me.outlawoasis.oasisheartbeat.model.HeartbeatModel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class HeartbeatCache {

    private static final Map<String, HeartbeatModel> cache = new ConcurrentHashMap<>();

    public static void put(String server, HeartbeatModel model) {
        cache.put(server, model);
    }

    public static HeartbeatModel get(String server) {
        return cache.get(server);
    }

    public static Map<String, HeartbeatModel> getAll() {
        return cache;
    }

    public static void remove(String server) {
        cache.remove(server);
    }
}
