package io.mindspice.data;

public record FullNutData(
        long lastTime,
        LinePoint[] voltageIn,
        LinePoint[] voltageOut,
        LinePoint[] power,
        LinePoint[] load,
        LinePoint[] charge,
        LinePoint[] runtime,
        LinePoint[] temperature,
        LinePoint[] online) {
}
