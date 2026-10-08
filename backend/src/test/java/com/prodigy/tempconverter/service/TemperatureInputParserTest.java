package com.prodigy.tempconverter.service;

import com.prodigy.tempconverter.exception.InvalidTemperatureException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TemperatureInputParserTest {

    @ParameterizedTest
    @CsvSource({
            "25,    25.0",
            "-4.5,  -4.5",
            "+3,    3.0",
            ".5,    0.5",
            "25.,   25.0",
            "0,     0.0"
    })
    void parsesValidDecimalNumbers(String input, double expected) {
        assertEquals(expected, TemperatureInputParser.parse(input), 1e-9);
    }

    @Test
    void ignoresSurroundingWhitespace() {
        assertEquals(25.0, TemperatureInputParser.parse("  25  "), 1e-9);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"  ", "abc", "25d", "25f", "NaN", "Infinity", "1e5",
            "0x10", "25.5.5", "--5", "2 5", "25,5"})
    void rejectsInvalidInput(String input) {
        assertThrows(InvalidTemperatureException.class, () -> TemperatureInputParser.parse(input));
    }
}