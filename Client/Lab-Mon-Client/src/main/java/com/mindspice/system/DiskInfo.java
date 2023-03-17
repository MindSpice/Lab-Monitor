package com.mindspice.system;

import com.mindspice.util.Utils;

import java.text.DecimalFormat;

public class DiskInfo {
    public String volume;
    public String mount;
    public double usableSpace;
    public double totalSpace;
    private final DecimalFormat df =Utils.getFormatter();
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("\tVolume: ").append(volume);
        sb.append("\n\tMount: ").append(mount);
        sb.append("\n\tUsable Space: ").append(df.format(usableSpace)).append("GB");
        sb.append("\n\tTotal Space: ").append(df.format(totalSpace)).append("GB");
        return sb.toString();
    }

    public DiskInfo(String volume, String mount, double usableSpace, double totalSpace) {
        this.volume = volume;
        this.mount = mount;
        this.usableSpace = usableSpace;
        this.totalSpace = totalSpace;

    }

    public DiskInfo() {
    }
}
