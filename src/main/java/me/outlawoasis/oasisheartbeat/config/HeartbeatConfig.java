package me.outlawoasis.oasisheartbeat.config;

import org.bukkit.plugin.java.JavaPlugin;

public class HeartbeatConfig {

    private final JavaPlugin plugin;

    public HeartbeatConfig(JavaPlugin plugin) {
        this.plugin = plugin;
        plugin.saveDefaultConfig();
    }

    public String getServerName() {
        return plugin.getConfig().getString("server.name", plugin.getServer().getName());
    }

    public int getPort() {
        return plugin.getConfig().getInt("server.port", plugin.getServer().getPort());
    }

    public boolean isHeartbeatEnabled() {
        return plugin.getConfig().getBoolean("heartbeat.enabled", true);
    }

    public int getIntervalSeconds() {
        return plugin.getConfig().getInt("heartbeat.interval", 5);
    }

    public int getTtlSeconds() {
        int ttl = plugin.getConfig().getInt("heartbeat.ttl", -1);
        if (ttl <= 0) {
            return getIntervalSeconds() * 2;
        }
        return ttl;
    }

    public VerboseLevel getVerboseLevel() {
        String raw = plugin.getConfig().getString("heartbeat.verbose", "error").toUpperCase();
        try {
            return VerboseLevel.valueOf(raw);
        } catch (IllegalArgumentException e) {
            return VerboseLevel.ERROR;
        }
    }

    public boolean isSenderEnabled() {
        return plugin.getConfig().getBoolean("sender.enabled", true);
    }

    public boolean isReaderEnabled() {
        return plugin.getConfig().getBoolean("reader.enabled", true);
    }

    public boolean isShowSelf() {
        return plugin.getConfig().getBoolean("reader.show-self", false);
    }

    public boolean isMeasurementEnabled(String key) {
        return plugin.getConfig().getBoolean("measurements." + key, true);
    }

    public String getOnlineText() {
        return plugin.getConfig().getString("status-text.online", "§aOnline");
    }

    public String getOfflineText() {
        return plugin.getConfig().getString("status-text.offline", "§cOffline");
    }


    public enum VerboseLevel {
        NONE,
        ERROR,
        ALL
    }
}
