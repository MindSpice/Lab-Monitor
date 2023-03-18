package io.mindspice.data;

public record FullClientData(
        String name,
        String address,
        LinePoint[] cpuSpeed,
        LinePoint[] cpuUsage,
        LinePoint[] memSwap,
        DiskData[] diskData){

}
