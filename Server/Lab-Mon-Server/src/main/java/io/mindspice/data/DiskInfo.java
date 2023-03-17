package io.mindspice.data;



import java.text.DecimalFormat;

public class DiskInfo {
    public String volume;
    public String mount;
    public double usableSpace;
    public double totalSpace;

    public DiskInfo(String volume, String mount, double usableSpace, double totalSpace) {
        this.volume = volume;
        this.mount = mount;
        this.usableSpace = usableSpace;
        this.totalSpace = totalSpace;
    }

    public DiskInfo() {
    }
}
