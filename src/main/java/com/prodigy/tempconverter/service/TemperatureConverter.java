package com.prodigy.tempconverter.service;

import com.prodigy.tempconverter.model.ConversionResult;
import com.prodigy.tempconverter.model.TemperatureUnit;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;

public class TemperatureConverter {

    public ConversionResult convert(double value, TemperatureUnit fromUnit) {
        Objects.requireNonNull(fromUnit, "fromUnit must not be null");

        double kelvin = fromUnit.toKelvin(value);

        Map<TemperatureUnit, Double> converted = new EnumMap<>(TemperatureUnit.class);
        for (TemperatureUnit target : TemperatureUnit.values()) {
            if (target != fromUnit) {
                converted.put(target, target.fromKelvin(kelvin));
            }
        }

        return new ConversionResult(value, fromUnit, Collections.unmodifiableMap(converted));
    }
}