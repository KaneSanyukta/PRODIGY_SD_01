package com.prodigy.tempconverter.model;

import java.util.Map;

public record ConversionResult(
        double inputValue,
        TemperatureUnit inputUnit,
        Map<TemperatureUnit, Double> convertedValues) {
}