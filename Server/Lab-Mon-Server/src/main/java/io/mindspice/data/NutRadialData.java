package io.mindspice.data;

public record NutRadialData(boolean online, String testResult, RadialData[] data ) {
    public record RadialData(String id, String label, int value){}
}
