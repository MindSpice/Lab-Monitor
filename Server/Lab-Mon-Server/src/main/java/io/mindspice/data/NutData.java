package io.mindspice.data;

import java.time.LocalTime;
import java.util.List;

public record NutData(
        boolean online,
        String status,
        double runtime,
        double charge,
        double load,
        double power,
        double inputVoltage,
        double outputVoltage,
        double temperature,
        String testResult,
        LocalTime time) {
}
