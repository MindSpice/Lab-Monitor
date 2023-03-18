package io.mindspice.data;

public record BriefClientData(
     boolean isConnected,
     String name,
     String address,
     String os,
     String cpuTemp,
     String cpuAvgSpeed,
     String cpuAvgUsage,
     String cpuThreads,
     String cpuProcesses,
     String memoryUsage,
     String swapUsage){
}
