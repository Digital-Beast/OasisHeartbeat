package me.outlawoasis.oasisheartbeat.reader;

import me.outlawoasis.oasiscore.api.OasisAPI;
import me.outlawoasis.oasiscore.util.JsonUtils;
import me.outlawoasis.oasiscore.util.TimeUtils;
import me.outlawoasis.oasisheartbeat.OasisHeartbeat;
import me.outlawoasis.oasisheartbeat.config.HeartbeatConfig;
import me.outlawoasis.oasisheartbeat.model.HeartbeatModel;
import me.outlawoasis.oasisheartbeat.reader.HeartbeatAPI;
import me.outlawoasis.oasisheartbeat.reader.HeartbeatCache;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.util.Set;
import java.util.stream.Collectors;

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
            // 1) Hent alle keys fra Redis
            Set<String> keys = api.getRedis().keys("oasis:heartbeat:*");

            // 2) Konverter keys til servernavne
            Set<String> activeServers = keys.stream()
                    .map(k -> k.replace("oasis:heartbeat:", ""))
                    .collect(Collectors.toSet());

            // 3) Læs alle aktive keys
            for (String key : keys) {

                String serverName = key.replace("oasis:heartbeat:", "");

                // skip self hvis disabled
                if (!config.isShowSelf() && serverName.equalsIgnoreCase(config.getServerName())) {
                    continue;
                }

                String json = api.getRedis().get(key);
                plugin.getLogger().info("[HB-READER] KEY=" + key + " JSON=" + json);

                if (json == null) {
                    // key findes ikke → fjern server
                    HeartbeatCache.remove(serverName);
                    HeartbeatAPI.remove(serverName);
                    continue;
                }

                HeartbeatModel model = JsonUtils.fromJson(json, HeartbeatModel.class);

                long now = System.currentTimeMillis();
                long expiry = model.getTimestamp() + (model.getTtl() * 1000L);

                if (now > expiry) {
                    // TTL udløbet → offline
                    model.setStatus("offline");
                    HeartbeatAPI.set(serverName, model);
                    continue;
                }

                // ellers online
                model.setStatus("online");
                HeartbeatAPI.set(serverName, model);

                if (config.getVerboseLevel() == HeartbeatConfig.VerboseLevel.ALL) {
                    plugin.getLogger().info("[HeartbeatReader] Updated: " + serverName);
                }
            }

            // 4) Cleanup: fjern servers der IKKE længere findes i Redis
            for (String cachedServer : HeartbeatAPI.getAllServers()) {
                if (!activeServers.contains(cachedServer)) {
                    HeartbeatAPI.markOffline(cachedServer);
                }
            }

        } catch (Exception e) {
            if (config.getVerboseLevel() != HeartbeatConfig.VerboseLevel.NONE) {
                plugin.getLogger().severe("[HeartbeatReader] Failed: " + e.getMessage());
            }
        }
    }
}
