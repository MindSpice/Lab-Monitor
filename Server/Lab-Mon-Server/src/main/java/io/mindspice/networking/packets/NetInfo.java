package io.mindspice.networking.packets;
import io.mindspice.data.DiskInfo;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

public class NetInfo {
    public String client;
    public double cpuTemp;
    public int cpuThreads;
    public int cpuProcesses;
    public double[] cpuSpeeds;
    public double cpuAvgSpeed;
    public double[] cpuUsage;
    public double cpuAvgUsage;
    public double[] memoryUsage;
    public double[] swapUsage;
    public DiskInfo[] diskInfo;
    public String time;

    public NetInfo(String client, double cpuTemp, int cpuThreads, int cpuProcesses, double[] cpuSpeeds, double cpuAvgSpeed,
                   double[] cpuUsage, double cpuAvgUsage, double[] memoryUsage, double[] swapUsage, DiskInfo[] diskInfo, String time) {
        this.client = client;
        this.cpuTemp = cpuTemp;
        this.cpuThreads = cpuThreads;
        this.cpuProcesses = cpuProcesses;
        this.cpuSpeeds = cpuSpeeds;
        this.cpuAvgSpeed = cpuAvgSpeed;
        this.cpuUsage = cpuUsage;
        this.cpuAvgUsage = cpuAvgUsage;
        this.memoryUsage = memoryUsage;
        this.swapUsage = swapUsage;
        this.diskInfo = diskInfo;
        this.time = time;
    }

    public NetInfo() {
    }
}
