package com.mindspice.util;

import com.mindspice.Settings;

import java.io.IOException;
import java.text.DecimalFormat;

public class Utils {
    final static int HZ_DIV = 1000000000;
    public final static int BYTE_MB_DIV = 1048576;
    public final static int BYTE_GB_DIV = 1073741824;
    private final static DecimalFormat df = new DecimalFormat("#.##");
    public static double hzToGhz(long hz) {
        return 1d * hz / HZ_DIV;
    }

    public static double hzToGhz(double hz) {
        return  hz / HZ_DIV;
    }

    public static double[] hzToGhz(long[] cores) {
        var rtnArr = new double[cores.length];
        for (int i = 0; i < cores.length; ++i) {
            rtnArr[i] = hzToGhz(cores[i]);
        }
        return rtnArr;
    }


    public static double bytesToMB(long bytes) {
        return 1d * bytes / BYTE_MB_DIV;
    }
    public static double bytesToGB(long bytes) {
        return 1d * bytes / BYTE_GB_DIV;
    }

    public static DecimalFormat getFormatter() {
        return df;
    }

    public static void shutdown() {
        System.out.println("Initiating Shutdown...");
        try {
            Runtime.getRuntime().exec(Settings.get().shutdownCommand);
        } catch (IOException e) {
            System.out.println("Shutdown Command Failed");
        }
        System.exit(0);
    }

}
