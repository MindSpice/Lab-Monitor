package com.mindspice.system;

import com.mindspice.Settings;
import com.mindspice.networking.packets.NetInfo;
import com.mindspice.util.Utils;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HardwareAbstractionLayer;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;

public class SysInfo {
    private static SysInfo instance = null;
    private final SystemInfo SYS_INFO = new SystemInfo();
    private final HardwareAbstractionLayer HW_LAYER = SYS_INFO.getHardware();
    private final CentralProcessor CPU = HW_LAYER.getProcessor();
    private final GlobalMemory MEMORY = HW_LAYER.getMemory();
    private volatile double[] cpuLoad = new double[]{};
    private final String OS_VERSION = SYS_INFO.getOperatingSystem().getFamily() + " | "  +
            SYS_INFO.getOperatingSystem().getVersionInfo().toString();


    private SysInfo(){
        new Thread(() -> {
            while (true) {
                try {
                    cpuLoad = CPU.getProcessorCpuLoad(2000);
                } catch (Exception ignored) {
                    // Ignored, should never happen.
                    // Though if issues arise, might be good to re-init the SystemInfo hook
                }
            }
        }).start();
    }

    public static SysInfo getInstance() {
        if (instance == null) {
            instance = new SysInfo();
        }
        return instance;
    }

    public String getSystemInfo() {
        return OS_VERSION;
    }

    public double[] getCoreSpeeds() {
        return Utils.hzToGhz(CPU.getCurrentFreq());
    }

    public double getAvgCoreSpeed() {
        return Utils.hzToGhz(Arrays.stream(CPU.getCurrentFreq()).average().orElse(0.0));
    }

    public double[] getMemoryUsage() {
        return new double[]{
                Utils.bytesToGB(MEMORY.getTotal() - MEMORY.getAvailable()),
                Utils.bytesToGB(MEMORY.getTotal())
        };
    }

    public double[] getSwapUsage() {
        return new double[]{
                Utils.bytesToGB(MEMORY.getVirtualMemory().getSwapUsed()),
                Utils.bytesToGB(MEMORY.getVirtualMemory().getSwapTotal())
        };
    }

    public double getTotalCoreUsage() {
        return  Arrays.stream(cpuLoad).average().orElse(0.0);
    }

    public double[] getCoreUsage() {
        return cpuLoad;
    }

    public int getThreadCount() {
        return SYS_INFO.getOperatingSystem().getThreadCount();
    }

    public int getProcessCount() {
        return SYS_INFO.getOperatingSystem().getProcessCount();
    }

    public double getCpuTemp() {
        return SYS_INFO.getHardware().getSensors().getCpuTemperature();
    }

    public DiskInfo[] getDiskInfo() {
        var disks = SYS_INFO.getOperatingSystem().getFileSystem().getFileStores();
        var diskInfo = new DiskInfo[disks.size()];
        for (int i = 0; i < disks.size(); ++i) {
            diskInfo[i] = new DiskInfo(
                    disks.get(i).getVolume(),
                    disks.get(i).getMount(),
                    Utils.bytesToGB(disks.get(i).getUsableSpace()),
                    Utils.bytesToGB(disks.get(i).getTotalSpace())
            );
        }
        return diskInfo;
    }

    public NetInfo getNetInfo() {
        return new NetInfo(
                Settings.get().clientName,
                getCpuTemp(),
                getThreadCount(),
                getProcessCount(),
                getCoreSpeeds(),
                getAvgCoreSpeed(),
                getCoreUsage(),
                getTotalCoreUsage(),
                getMemoryUsage(),
                getSwapUsage(),
                Settings.get().sendDiskInfo ? getDiskInfo() : null,
                LocalTime.now().truncatedTo(ChronoUnit.SECONDS).toString()
        );
    }
}
