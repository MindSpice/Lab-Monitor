package io.mindspice.data;

import io.mindspice.networking.packets.NetInfo;

import java.util.List;

public record FullClientData(
        String name,
        String os,
        String address,
        LinePoint[] cpuSpeed,
        LinePoint[] cpuUsage,
        LinePoint[] memSwap){
}
