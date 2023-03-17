package io.mindspice.utils;

import java.text.DecimalFormat;

public class Utils {
    private final static DecimalFormat df = new DecimalFormat("#.##");

    public static DecimalFormat getFormatter() {
        return df;
    }

}
