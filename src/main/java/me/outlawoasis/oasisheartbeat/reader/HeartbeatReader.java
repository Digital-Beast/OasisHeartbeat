package me.outlawoasis.oasisheartbeat.reader;

import me.outlawoasis.oasiscore.api.OasisAPI;
import me.outlawoasis.oasiscore.util.JsonUtils;
import me.outlawoasis.oasiscore.util.TimeUtils;
import me.outlawoasis.oasisheartbeat.OasisHeartbeat;
import me.outlawoasis.oasisheartbeat.config.HeartbeatConfig;
import me.outlawoasis.oasisheartbeat.model.HeartbeatModel;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.util.Set;

public class HeartbeatReader {

    private final Plugin plugin;
    private final OasisAPI api;
    private final HeartbeatConfig config;

    public HeartbeatReader(Plugin plugin, OasisAPI api, HeartbeatConfig config) {
        this.plugin = plugin;
        this.api = api;
        this.config = config;
    }

    public void start() {
        long intervalTicks = TimeUtils.seconds(config.getIntervalSeconds());

        Bukkit.getScheduler().runTaskTimerAsynchronously(
                plugin,
                this::readHeartbeats,
                0L,
                intervalTicks
        );
    }

    private void readHeartbeats() {
        try {
            Set<String> keys = api.getRedis().keys("oasis:heartbeat:*");

            for (String key : keys) {

                String serverName = key.replace("oasis:heartbeat:", "");

                // skip self if disabled
                if (!config.isShowSelf() && serverName.equalsIgnoreCase(config.getServerName())) {
                    continue;
                }

                String json = api.getRedis().get(key);
                if (json == null) {
                    HeartbeatCache.remove(serverName);
                    continue;
                }

                HeartbeatModel model = JsonUtils.fromJson(json, HeartbeatModel.class);

                long now = System.currentTimeMillis();

                long expiry = model.getTimestamp() + (model.getTtl() * 1000L);
                if (now > expiry) {
                    model.setStatus("offline");
                } else {
                    model.setStatus("online");
                }

                HeartbeatCache.put(serverName, model);

                if (config.getVerboseLevel() == HeartbeatConfig.VerboseLevel.ALL) {
                    plugin.getLogger().info("[HeartbeatReader] Updated: " + serverName);
                }
            }

        } catch (Exception e) {
            if (config.getVerboseLevel() != HeartbeatConfig.VerboseLevel.NONE) {
                plugin.getLogger().severe("[HeartbeatReader] Failed: " + e.getMessage());
            }
        }
    }
}
