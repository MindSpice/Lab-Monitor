package io.mindspice.data;

public record NutData(
        String status,
        int runtime,
        int charge,
        int load,
        int power,
        float inputVoltage,
        float outputVoltage,
        float temperature,
        String testResult) {
}
