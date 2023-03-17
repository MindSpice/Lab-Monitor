package io.mindspice.networking.packets;

public class Handshake {
    public String name;
    public String os;
    public boolean sendWakeOnLan;
    public boolean sendShutdownInfo;
    public int wakeThreshold;

    public Handshake(String name, String os, boolean sendWakeOnLan, boolean sendShutdownInfo, int wakeThreshold) {
        this.name = name;
        this.os = os;
        this.sendWakeOnLan = sendWakeOnLan;
        this.sendShutdownInfo = sendShutdownInfo;
        this.wakeThreshold = wakeThreshold;
    }

    public Handshake() {
    }
}
