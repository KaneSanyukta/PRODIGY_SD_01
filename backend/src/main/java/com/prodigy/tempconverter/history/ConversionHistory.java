package com.prodigy.tempconverter.history;

import com.prodigy.tempconverter.model.ConversionResult;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.Objects;

public class ConversionHistory {

    public static final int MAX_ENTRIES = 50;

    private final Deque<ConversionResult> entries = new ArrayDeque<>();

    public void add(ConversionResult result) {
        Objects.requireNonNull(result, "result must not be null");
        entries.addFirst(result);
        if (entries.size() > MAX_ENTRIES) {
            entries.removeLast();
        }
    }

    /** Returns an unmodifiable snapshot, newest entry first. */
    public List<ConversionResult> getAll() {
        return List.copyOf(entries);
    }

    public int size() {
        return entries.size();
    }

    public boolean isEmpty() {
        return entries.isEmpty();
    }

    public void clear() {
        entries.clear();
    }
}