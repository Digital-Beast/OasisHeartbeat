package me.outlawoasis.oasisheartbeat.reader;

import me.outlawoasis.oasisheartbeat.model.HeartbeatModel;

import java.util.Map;

public class HeartbeatAPI {

    public static HeartbeatModel get(String server) {
        return HeartbeatCache.get(server);
    }

    public static Map<String, HeartbeatModel> getAll() {
        return HeartbeatCache.getAll();
    }
}
