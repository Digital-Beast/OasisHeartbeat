package me.outlawoasis.oasisheartbeat.heartbeat;

import me.outlawoasis.oasiscore.api.OasisAPI;
import me.outlawoasis.oasiscore.util.JsonUtils;
import me.outlawoasis.oasiscore.util.TimeUtils;
import me.outlawoasis.oasisheartbeat.config.HeartbeatConfig;
import me.outlawoasis.oasisheartbeat.model.HeartbeatModel;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class HeartbeatSender {

    private final Plugin plugin;
    private final OasisAPI api;
    private final HeartbeatConfig config;

    private long startTime;

    public HeartbeatSender(Plugin plugin, OasisAPI api, HeartbeatConfig config) {
        this.plugin = plugin;
        this.api = api;
        this.config = config;
        this.startTime = System.currentTimeMillis();
    }

    public void start() {
        long intervalTicks = TimeUtils.seconds(config.getIntervalSeconds());

        Bukkit.getScheduler().runTaskTimerAsynchronously(
                plugin,
                this::sendHeartbeat,
                0L,
                intervalTicks
        );
    }

    private void sendHeartbeat() {
        try {
            // 1) Collect all measurements
            HeartbeatModel model = me.outlawoasis.oasisheartbeat.heartbeat.HeartbeatCollector.collect(config, startTime);

            // 2) Convert to JSON
            String json = JsonUtils.toJson(model);

            // 3) Redis key
            String key = "oasis:heartbeat:" + config.getServerName();

            // 4) Write to Redis (SET + EXPIRE)
            api.getRedis().set(key, json, config.getTtlSeconds());

            // 5) Verbose logging
            if (config.getVerboseLevel() == HeartbeatConfig.VerboseLevel.ALL) {
                plugin.getLogger().info("[Heartbeat] Sent: " + json);
            }

        } catch (Exception e) {
            if (config.getVerboseLevel() != HeartbeatConfig.VerboseLevel.NONE) {
                plugin.getLogger().severe("[Heartbeat] Failed to send heartbeat: " + e.getMessage());
            }
        }
    }
}
