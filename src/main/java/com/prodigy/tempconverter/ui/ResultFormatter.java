package com.prodigy.tempconverter.ui;

import com.prodigy.tempconverter.model.ConversionResult;
import com.prodigy.tempconverter.model.TemperatureUnit;

import java.util.Locale;
import java.util.stream.Collectors;

public final class ResultFormatter {

    private ResultFormatter() {
    }

    /** Multi-line text: the input, then one line per converted unit. */
    public static String details(ConversionResult result) {
        StringBuilder sb = new StringBuilder();
        sb.append(line("Input", result.inputValue(), result.inputUnit()));
        result.convertedValues().forEach((unit, value) ->
                sb.append(line(unit.getDisplayName(), value, unit)));
        return sb.toString();
    }

    /** One-line text for history, e.g. "25.00 C -> 77.00 F | 298.15 K". */
    public static String summary(ConversionResult result) {
        String converted = result.convertedValues().entrySet().stream()
                .map(e -> number(e.getValue()) + " " + e.getKey().getSymbol())
                .collect(Collectors.joining(" | "));
        return number(result.inputValue()) + " " + result.inputUnit().getSymbol()
                + "  \u2192  " + converted;
    }

    private static String line(String label, double value, TemperatureUnit unit) {
        return String.format("%-11s: %s %s%n", label, number(value), unit.getSymbol());
    }

    private static String number(double value) {
        String text = String.format(Locale.ROOT, "%.2f", value);
        return text.equals("-0.00") ? "0.00" : text;
    }
}