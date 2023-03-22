package io.mindspice.data;

import java.time.LocalTime;

public record FullClientData(
        long lastTime,
        String name,
        String address,
        LinePoint[] cpuSpeed,
        LinePoint[] cpuUsage,
        LinePoint[] memSwap,
        DiskData[] diskData){
}
