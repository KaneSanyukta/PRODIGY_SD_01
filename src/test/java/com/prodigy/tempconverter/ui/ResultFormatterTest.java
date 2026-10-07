package com.prodigy.tempconverter.ui;

import com.prodigy.tempconverter.model.ConversionResult;
import com.prodigy.tempconverter.model.TemperatureUnit;
import com.prodigy.tempconverter.service.TemperatureConverter;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ResultFormatterTest {

    private final TemperatureConverter converter = new TemperatureConverter();

    @Test
    void detailsShowInputAndBothConvertedUnits() {
        ConversionResult result = converter.convert(25, TemperatureUnit.CELSIUS);

        String text = ResultFormatter.details(result);

        assertTrue(text.contains("25.00 \u00B0C"));
        assertTrue(text.contains("77.00 \u00B0F"));
        assertTrue(text.contains("298.15 K"));
    }

    @Test
    void tinyNegativeValuesAreNotShownAsNegativeZero() {
        ConversionResult result = converter.convert(-0.001, TemperatureUnit.CELSIUS);

        String text = ResultFormatter.details(result);

        assertTrue(text.contains("0.00 \u00B0C"));
        assertFalse(text.contains("-0.00"));
    }
}