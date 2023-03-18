package com.mindspice.networking.packets;
import java.util.List;

public class Handshake {
    public String name;
    public String os;
    public String[] macAddress;
    public boolean sendWakeOnLan;
    public boolean sendShutdownInfo;
    public int wakeThreshold;

    public Handshake(String name, String os, String[] macAddress, boolean sendWakeOnLan,
                     boolean sendShutdownInfo, int wakeThreshold) {
        this.name = name;
        this.os = os;
        this.macAddress = macAddress;
        this.sendWakeOnLan = sendWakeOnLan;
        this.sendShutdownInfo = sendShutdownInfo;
        this.wakeThreshold = wakeThreshold;
    }

    public Handshake() {
    }
}
