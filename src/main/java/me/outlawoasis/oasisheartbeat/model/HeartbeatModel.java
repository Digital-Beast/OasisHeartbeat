package me.outlawoasis.oasisheartbeat.model;

public class HeartbeatModel {

    private String status; // "online" eller "offline"


    // MUST
    private String server;
    private long timestamp;
    private int ttl;

    // META
    private int port;
    private long uptime;

    // MEASUREMENTS
    private Integer players;
    private Integer maxPlayers;
    private Double tps;
    private Double mspt;
    private Double cpu;
    private Long ram;
    private Integer ping;
    private Integer chunks;
    private Integer worlds;

    // GETTERS + SETTERS

    public String getServer() { return server; }
    public void setServer(String server) { this.server = server; }

    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }

    public int getTtl() { return ttl; }
    public void setTtl(int ttl) { this.ttl = ttl; }

    public int getPort() { return port; }
    public void setPort(int port) { this.port = port; }

    public long getUptime() { return uptime; }
    public void setUptime(long uptime) { this.uptime = uptime; }

    public Integer getPlayers() { return players; }
    public void setPlayers(Integer players) { this.players = players; }

    public Integer getMaxPlayers() { return maxPlayers; }
    public void setMaxPlayers(Integer maxPlayers) { this.maxPlayers = maxPlayers; }

    public Double getTps() { return tps; }
    public void setTps(Double tps) { this.tps = tps; }

    public Double getMspt() { return mspt; }
    public void setMspt(Double mspt) { this.mspt = mspt; }

    public Double getCpu() { return cpu; }
    public void setCpu(Double cpu) { this.cpu = cpu; }

    public Long getRam() { return ram; }
    public void setRam(Long ram) { this.ram = ram; }

    public Integer getPing() { return ping; }
    public void setPing(Integer ping) { this.ping = ping; }

    public Integer getChunks() { return chunks; }
    public void setChunks(Integer chunks) { this.chunks = chunks; }

    public Integer getWorlds() { return worlds; }
    public void setWorlds(Integer worlds) { this.worlds = worlds; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

}



