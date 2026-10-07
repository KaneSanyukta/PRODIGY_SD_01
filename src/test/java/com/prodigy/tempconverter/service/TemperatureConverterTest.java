package com.prodigy.tempconverter.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import org.junit.jupiter.params.provider.ValueSource;
import com.prodigy.tempconverter.exception.InvalidTemperatureException;
import com.prodigy.tempconverter.model.ConversionResult;
import com.prodigy.tempconverter.model.TemperatureUnit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TemperatureConverterTest {

    private static final double DELTA = 1e-9;

    private TemperatureConverter converter;

    @BeforeEach
    void setUp() {
        converter = new TemperatureConverter();
    }

    @ParameterizedTest(name = "{1} {0} = {3} {2}")
    @CsvSource({
            "CELSIUS,     25,   FAHRENHEIT,  77.0",
            "CELSIUS,     25,   KELVIN,      298.15",
            "CELSIUS,     100,  FAHRENHEIT,  212.0",
            "CELSIUS,     100,  KELVIN,      373.15",
            "CELSIUS,     0,    FAHRENHEIT,  32.0",
            "CELSIUS,     0,    KELVIN,      273.15",
            "CELSIUS,     -40,  FAHRENHEIT,  -40.0",
            "FAHRENHEIT,  -40,  CELSIUS,     -40.0",
            "FAHRENHEIT,  32,   CELSIUS,     0.0",
            "FAHRENHEIT,  212,  CELSIUS,     100.0",
            "KELVIN,      300,  CELSIUS,     26.85",
            "KELVIN,      300,  FAHRENHEIT,  80.33",
            "KELVIN,      0,    CELSIUS,     -273.15",
            "KELVIN,      0,    FAHRENHEIT,  -459.67"
    })
    void convertsKnownValuesCorrectly(TemperatureUnit from, double value,
                                      TemperatureUnit to, double expected) {
        ConversionResult result = converter.convert(value, from);

        assertEquals(expected, result.convertedValues().get(to), DELTA);
    }
    
    @ParameterizedTest
    @CsvSource({
            "CELSIUS,    -273.16",
            "FAHRENHEIT, -459.68",
            "KELVIN,     -0.01"
    })
    void rejectsValuesBelowAbsoluteZero(TemperatureUnit unit, double value) {
        assertThrows(InvalidTemperatureException.class, () -> converter.convert(value, unit));
    }

    @ParameterizedTest
    @CsvSource({
            "CELSIUS,    -273.15",
            "FAHRENHEIT, -459.67"
    })
    void absoluteZeroIsAcceptedAndGivesExactlyZeroKelvin(TemperatureUnit unit, double value) {
        ConversionResult result = converter.convert(value, unit);

        assertEquals(0.0, result.convertedValues().get(TemperatureUnit.KELVIN), 0.0);
    }

    @Test
    void zeroKelvinIsAccepted() {
        assertDoesNotThrow(() -> converter.convert(0, TemperatureUnit.KELVIN));
    }

    @ParameterizedTest
    @ValueSource(doubles = {Double.NaN, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY, 1e13, -1e13})
    void rejectsNonFiniteAndHugeValues(double value) {
        assertThrows(InvalidTemperatureException.class,
                () -> converter.convert(value, TemperatureUnit.CELSIUS));
    }

    @Test
    void resultContainsOnlyTheTwoOtherUnits() {
        ConversionResult result = converter.convert(25, TemperatureUnit.CELSIUS);

        assertEquals(2, result.convertedValues().size());
        assertFalse(result.convertedValues().containsKey(TemperatureUnit.CELSIUS));
        assertTrue(result.convertedValues().containsKey(TemperatureUnit.FAHRENHEIT));
        assertTrue(result.convertedValues().containsKey(TemperatureUnit.KELVIN));
    }

    @Test
    void resultKeepsTheOriginalInput() {
        ConversionResult result = converter.convert(25, TemperatureUnit.CELSIUS);

        assertEquals(25.0, result.inputValue(), DELTA);
        assertEquals(TemperatureUnit.CELSIUS, result.inputUnit());
    }

    @Test
    void roundTripReturnsTheOriginalValue() {
        double fahrenheit = converter.convert(25, TemperatureUnit.CELSIUS)
                .convertedValues().get(TemperatureUnit.FAHRENHEIT);

        double backToCelsius = converter.convert(fahrenheit, TemperatureUnit.FAHRENHEIT)
                .convertedValues().get(TemperatureUnit.CELSIUS);

        assertEquals(25.0, backToCelsius, DELTA);
    }

    @Test
    void nullUnitIsRejected() {
        assertThrows(NullPointerException.class, () -> converter.convert(25, null));
    }
}