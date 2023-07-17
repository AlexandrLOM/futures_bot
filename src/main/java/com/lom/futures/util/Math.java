package com.lom.futures.util;

import com.lom.futures.enums.Symbol;

public class Math {

    public static Double round(Double value, Integer roundTo) {
        var multiplier = java.lang.Math.pow(10, roundTo);
        return java.lang.Math.round(value * multiplier) / multiplier;
    }

    public static Double round(Double value, Integer roundTo, Symbol symbol) {
        switch (symbol) {
            case ETHUSDT -> roundTo = roundTo > 2 ? 2 : roundTo;
            case BTCUSDT, DEFIUSDT -> roundTo = roundTo > 1 ? 1 : roundTo;
        }

        var multiplier = java.lang.Math.pow(10, roundTo);
        return java.lang.Math.round(value * multiplier) / multiplier;
    }
}
