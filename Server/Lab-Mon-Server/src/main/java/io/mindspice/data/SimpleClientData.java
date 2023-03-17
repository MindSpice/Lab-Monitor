package io.mindspice.data;

public record SimpleClientData(
     boolean isConnected,
     String name,
     String address,
     String cpuTemp,
     String cpuAvgSpeed,
     String cpuAvgUsage,
     String cpuThreads,
     String cpuProcesses,
     String memoryUsage,
     String swapUsage){
}
