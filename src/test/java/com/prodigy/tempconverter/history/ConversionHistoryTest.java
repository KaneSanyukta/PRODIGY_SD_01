package com.prodigy.tempconverter.history;

import com.prodigy.tempconverter.model.ConversionResult;
import com.prodigy.tempconverter.model.TemperatureUnit;
import com.prodigy.tempconverter.service.TemperatureConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ConversionHistoryTest {

    private final TemperatureConverter converter = new TemperatureConverter();
    private ConversionHistory history;

    @BeforeEach
    void setUp() {
        history = new ConversionHistory();
    }

    private ConversionResult resultFor(double celsius) {
        return converter.convert(celsius, TemperatureUnit.CELSIUS);
    }

    @Test
    void startsEmpty() {
        assertTrue(history.isEmpty());
        assertEquals(0, history.size());
    }

    @Test
    void returnsNewestFirst() {
        history.add(resultFor(10));
        history.add(resultFor(20));

        List<ConversionResult> all = history.getAll();

        assertEquals(20.0, all.get(0).inputValue(), 1e-9);
        assertEquals(10.0, all.get(1).inputValue(), 1e-9);
    }

    @Test
    void dropsOldestEntriesBeyondTheLimit() {
        int total = ConversionHistory.MAX_ENTRIES + 10;
        for (int i = 0; i < total; i++) {
            history.add(resultFor(i));
        }

        List<ConversionResult> all = history.getAll();

        assertEquals(ConversionHistory.MAX_ENTRIES, history.size());
        assertEquals(total - 1, all.get(0).inputValue(), 1e-9);
        assertEquals(10.0, all.get(all.size() - 1).inputValue(), 1e-9);
    }

    @Test
    void returnedListCannotBeModified() {
        history.add(resultFor(10));

        assertThrows(UnsupportedOperationException.class,
                () -> history.getAll().add(resultFor(20)));
    }

    @Test
    void clearRemovesEverything() {
        history.add(resultFor(10));
        history.clear();

        assertTrue(history.isEmpty());
    }

    @Test
    void nullResultIsRejected() {
        assertThrows(NullPointerException.class, () -> history.add(null));
    }
}