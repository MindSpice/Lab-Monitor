package com.mindspice.monitor;

import com.mindspice.Settings;
import com.mindspice.system.SysInfo;
import com.mindspice.util.Utils;

import java.text.DecimalFormat;

public class LocalMonitor implements Runnable {

   DecimalFormat df = Utils.getFormatter();
    @Override
    public void run() {
        try {
            final StringBuilder sb = new StringBuilder();
            final SysInfo sys = SysInfo.getInstance();
            sb.append("System Name: ").append(Settings.get().clientName);
            sb.append("\nSystem Info:").append(sys.getSystemInfo());
            sb.append("\n\nProcessor:");
            sb.append("\n\tTotal Processes: ").append(sys.getProcessCount()).append(" | Total Treads: ").append(sys.getThreadCount());
            sb.append(" | Temp: ").append(Math.round(sys.getCpuTemp())).append("C");
            sb.append("\n\tTotal Speed: ").append(df.format(sys.getAvgCoreSpeed())).append(" GHz");
                    sb.append(" | Total Usage: ").append(df.format(sys.getTotalCoreUsage())).append("%");

            var cSpeed = sys.getCoreSpeeds();
            var cUsage = sys.getCoreUsage();
            for (int i = 0; i < cSpeed.length; ++i) {
                sb.append("\n\tCore ").append(i).append(": ").append(df.format(cSpeed[i])).append(" GHz").append(" | ").append(df.format(cUsage[i])).append("%");
            }

            var mem = sys.getMemoryUsage();
            var swap = sys.getSwapUsage();
            sb.append("\n\nMemory:");
            sb.append("\n\t").append(df.format(mem[0])).append("/").append(df.format(mem[1])).append(" GB");
            sb.append("\nSwap:");
            sb.append("\n\t").append(df.format(swap[0])).append("/").append(df.format(swap[1])).append(" GB");

            var disks = sys.getDiskInfo();
            sb.append("\n\nDisks:");
            for (var disk : disks) {
                sb.append("\n\n").append(disk);
            }
            System.out.println(sb);
        } catch (Exception ignored) {
            ignored.printStackTrace();
        }
    }
}
