package com.prodigy.tempconverter.service;

import com.prodigy.tempconverter.exception.InvalidTemperatureException;

import java.util.regex.Pattern;

public final class TemperatureInputParser {

    private static final Pattern DECIMAL_NUMBER = Pattern.compile("[+-]?(\\d+(\\.\\d*)?|\\.\\d+)");

    private TemperatureInputParser() {
    }

    public static double parse(String text) {
        if (text == null || text.isBlank()) {
            throw new InvalidTemperatureException("Please enter a temperature value.");
        }

        String trimmed = text.trim();
        if (!DECIMAL_NUMBER.matcher(trimmed).matches()) {
            throw new InvalidTemperatureException("Invalid number. Use digits only, for example 25 or -4.5");
        }

        return Double.parseDouble(trimmed);
    }
}