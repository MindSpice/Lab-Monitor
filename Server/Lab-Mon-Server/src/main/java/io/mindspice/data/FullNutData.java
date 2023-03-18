package io.mindspice.data;

public record FullNutData(
    LinePoint[] voltageIn,
    LinePoint[] voltageOut,
    LinePoint[] power,
    LinePoint[] load,
    LinePoint[] charge,
    LinePoint[] runtime,
    LinePoint[] temperature,
    LinePoint[] online) {
}
