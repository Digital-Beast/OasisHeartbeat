package me.outlawoasis.oasisheartbeat.placeholder;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.outlawoasis.oasisheartbeat.OasisHeartbeat;
import me.outlawoasis.oasisheartbeat.model.HeartbeatModel;
import me.outlawoasis.oasisheartbeat.reader.HeartbeatAPI;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class HeartbeatExpansion extends PlaceholderExpansion {

    private final OasisHeartbeat plugin;

    public HeartbeatExpansion(OasisHeartbeat plugin) {
        this.plugin = plugin;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "hb";
    }

    @Override
    public @NotNull String getAuthor() {
        return "OutlawOasis";
    }

    @Override
    public @NotNull String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public boolean persist() {
        return true; // reload-safe
    }

    @Override
    public String onPlaceholderRequest(Player player, @NotNull String params) {

        // SELF placeholders
        if (params.startsWith("self_")) {
            String field = params.substring(5);
            HeartbeatModel self = HeartbeatAPI.get(plugin.getHeartbeatConfig().getServerName());
            return resolveField(self, field);
        }

        // SERVER placeholders: <server>_<field>
        if (params.contains("_")) {
            String[] parts = params.split("_", 2);
            String server = parts[0];
            String field = parts[1];

            HeartbeatModel model = HeartbeatAPI.get(server);
            return resolveField(model, field);
        }

        return null;
    }

    private String resolveField(HeartbeatModel model, String field) {
        if (model == null) return "N/A";

        return switch (field.toLowerCase()) {
            case "players" -> String.valueOf(model.getPlayers());
            case "maxplayers" -> String.valueOf(model.getMaxPlayers());
            case "tps" -> String.valueOf(model.getTps());
            case "mspt" -> String.valueOf(model.getMspt());
            case "cpu" -> String.valueOf(model.getCpu());
            case "ram" -> String.valueOf(model.getRam());
            case "ping" -> String.valueOf(model.getPing());
            case "chunks" -> String.valueOf(model.getChunks());
            case "worlds" -> String.valueOf(model.getWorlds());
            case "uptime" -> String.valueOf(model.getUptime());
            case "timestamp" -> String.valueOf(model.getTimestamp());
            case "ttl" -> String.valueOf(model.getTtl());
            case "status" -> model.getStatus().equalsIgnoreCase("online")
                    ? plugin.getHeartbeatConfig().getOnlineText()
                    : plugin.getHeartbeatConfig().getOfflineText();

            default -> "N/A";
        };
    }
}
