package com.prodigy.tempconverter.service;

import com.prodigy.tempconverter.exception.InvalidTemperatureException;
import com.prodigy.tempconverter.model.ConversionResult;
import com.prodigy.tempconverter.model.TemperatureUnit;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;

public class TemperatureConverter {

    private static final double TOLERANCE = 1e-9;
    private static final double MAX_ABSOLUTE_VALUE = 1_000_000_000_000.0;

    public ConversionResult convert(double value, TemperatureUnit fromUnit) {
        Objects.requireNonNull(fromUnit, "fromUnit must not be null");

        double kelvin = validatedKelvin(value, fromUnit);

        Map<TemperatureUnit, Double> converted = new EnumMap<>(TemperatureUnit.class);
        for (TemperatureUnit target : TemperatureUnit.values()) {
            if (target != fromUnit) {
                converted.put(target, target.fromKelvin(kelvin));
            }
        }

        return new ConversionResult(value, fromUnit, Collections.unmodifiableMap(converted));
    }

    private double validatedKelvin(double value, TemperatureUnit unit) {
        if (!Double.isFinite(value) || Math.abs(value) > MAX_ABSOLUTE_VALUE) {
            throw new InvalidTemperatureException("Temperature value is out of the supported range.");
        }

        double kelvin = unit.toKelvin(value);

        if (kelvin < -TOLERANCE) {
            double absoluteZero = unit.fromKelvin(0.0);
            throw new InvalidTemperatureException(String.format(
                    "%.2f %s is below absolute zero (%.2f %s).",
                    value, unit.getSymbol(), absoluteZero, unit.getSymbol()));
        }

        return Math.abs(kelvin) < TOLERANCE ? 0.0 : kelvin;
    }
}