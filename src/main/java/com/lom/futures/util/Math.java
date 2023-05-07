package com.lom.futures.util;

public class Math {

    public static Double round(Double value, Integer roundTo) {
        var multiplier = java.lang.Math.pow(10, roundTo);
        return java.lang.Math.round(value * multiplier) / multiplier;
    }
}
