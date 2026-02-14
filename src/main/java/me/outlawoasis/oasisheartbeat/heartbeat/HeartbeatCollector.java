package me.outlawoasis.oasisheartbeat.heartbeat;

import me.outlawoasis.oasisheartbeat.config.HeartbeatConfig;
import me.outlawoasis.oasisheartbeat.model.HeartbeatModel;
import org.bukkit.Bukkit;
import org.bukkit.World;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;

public class HeartbeatCollector {

    public static HeartbeatModel collect(HeartbeatConfig config, long startTime) {

        HeartbeatModel model = new HeartbeatModel();


        model.setStatus("online");
        // MUST
        model.setServer(config.getServerName());
        model.setTimestamp(System.currentTimeMillis());
        model.setTtl(config.getTtlSeconds());

        // META
        model.setPort(config.getPort());
        model.setUptime(System.currentTimeMillis() - startTime);

        // MEASUREMENTS
        if (config.isMeasurementEnabled("players"))
            model.setPlayers(Bukkit.getOnlinePlayers().size());

        if (config.isMeasurementEnabled("maxplayers"))
            model.setMaxPlayers(Bukkit.getMaxPlayers());

        if (config.isMeasurementEnabled("tps"))
            model.setTps(Bukkit.getTPS()[0]);

        if (config.isMeasurementEnabled("mspt"))
            model.setMspt(Bukkit.getAverageTickTime());

        if (config.isMeasurementEnabled("cpu")) {
            OperatingSystemMXBean os = ManagementFactory.getOperatingSystemMXBean();
            try {
                double load = os.getSystemLoadAverage();
                model.setCpu(load);
            } catch (Exception ignored) {}
        }

        if (config.isMeasurementEnabled("ram")) {
            long used = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
            model.setRam(used);
        }

        if (config.isMeasurementEnabled("ping")) {
            int total = 0;
            int count = 0;
            for (var p : Bukkit.getOnlinePlayers()) {
                total += p.getPing();
                count++;
            }
            model.setPing(count == 0 ? 0 : total / count);
        }

        if (config.isMeasurementEnabled("chunks")) {
            int chunks = 0;
            for (World w : Bukkit.getWorlds()) {
                chunks += w.getLoadedChunks().length;
            }
            model.setChunks(chunks);
        }

        if (config.isMeasurementEnabled("worlds"))
            model.setWorlds(Bukkit.getWorlds().size());

        return model;
    }
}
