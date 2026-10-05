package com.prodigy.tempconverter;

import com.prodigy.tempconverter.model.ConversionResult;
import com.prodigy.tempconverter.model.TemperatureUnit;
import com.prodigy.tempconverter.service.TemperatureConverter;

public class Main {
    public static void main(String[] args) {
        TemperatureConverter converter = new TemperatureConverter();
        ConversionResult result = converter.convert(25, TemperatureUnit.CELSIUS);

        System.out.printf("Input: %.2f %s%n", result.inputValue(), result.inputUnit().getDisplayName());
        result.convertedValues().forEach((unit, value) ->
                System.out.printf("%s: %.2f%n", unit.getDisplayName(), value));
    }
}