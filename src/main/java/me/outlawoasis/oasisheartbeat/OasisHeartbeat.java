package me.outlawoasis.oasisheartbeat;

import me.outlawoasis.oasiscore.OasisCore;
import me.outlawoasis.oasiscore.api.OasisAPI;
import me.outlawoasis.oasisheartbeat.placeholder.HeartbeatExpansion;
import me.outlawoasis.oasisheartbeat.config.HeartbeatConfig;
import me.outlawoasis.oasisheartbeat.heartbeat.HeartbeatSender;
import me.outlawoasis.oasisheartbeat.reader.HeartbeatReader;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class OasisHeartbeat extends JavaPlugin {

    private static OasisHeartbeat instance;

    private OasisAPI api;
    private HeartbeatConfig config;

    @Override
    public void onEnable() {
        instance = this;

        api = OasisCore.get().getAPI();
        config = new HeartbeatConfig(this);

        getLogger().info("OasisHeartbeat enabled. Using OasisCore API.");

        if (config.isSenderEnabled() && config.isHeartbeatEnabled()) {
            new HeartbeatSender(this, api, config).start();
        }

        if (config.isReaderEnabled()) {
            new HeartbeatReader(this, api, config).start();

                if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
                    new HeartbeatExpansion(this).register();
                    getLogger().info("PlaceholderAPI hook enabled.");
                }


        }


    }

    @Override
    public void onDisable() {
        String key = "oasis:heartbeat:" + config.getServerName(); api.getRedis().delete(key);
    }

    public static OasisHeartbeat get() {
        return instance;
    }

    public OasisAPI getOasisAPI() {
        return api;
    }

    public HeartbeatConfig getHeartbeatConfig() {
        return config;
    }
}
