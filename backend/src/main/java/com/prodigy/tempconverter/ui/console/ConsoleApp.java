package com.prodigy.tempconverter.ui.console;

import com.prodigy.tempconverter.exception.InvalidTemperatureException;
import com.prodigy.tempconverter.history.ConversionHistory;
import com.prodigy.tempconverter.model.ConversionResult;
import com.prodigy.tempconverter.model.TemperatureUnit;
import com.prodigy.tempconverter.service.TemperatureConverter;
import com.prodigy.tempconverter.service.TemperatureInputParser;
import com.prodigy.tempconverter.ui.ResultFormatter;

import java.io.PrintStream;
import java.util.Locale;
import java.util.Optional;
import java.util.Scanner;

public class ConsoleApp {

    private final TemperatureConverter converter;
    private final ConversionHistory history;
    private final Scanner in;
    private final PrintStream out;

    public ConsoleApp(TemperatureConverter converter, ConversionHistory history,
                      Scanner in, PrintStream out) {
        this.converter = converter;
        this.history = history;
        this.in = in;
        this.out = out;
    }

    public void run() {
        out.println("=== Temperature Converter ===");
        boolean running = true;
        while (running) {
            printMenu();
            String choice = readLine("Choose an option: ");
            if (choice == null) {
                break; // end of input
            }
            switch (choice.trim()) {
                case "1" -> convertFlow();
                case "2" -> showHistory();
                case "3" -> running = false;
                default -> out.println("Please choose 1, 2 or 3.");
            }
        }
        out.println("Goodbye!");
    }

    private void printMenu() {
        out.println();
        out.println("1) Convert a temperature");
        out.println("2) Show history");
        out.println("3) Exit");
    }

    private void convertFlow() {
        while (true) {
            Double value = promptValue();
            if (value == null) {
                return;
            }
            TemperatureUnit unit = promptUnit();
            if (unit == null) {
                return;
            }
            try {
                ConversionResult result = converter.convert(value, unit);
                history.add(result);
                out.println();
                out.print(ResultFormatter.details(result));
                out.println();
            } catch (InvalidTemperatureException ex) {
                out.println("Error: " + ex.getMessage());
                continue; // ask for a new value straight away
            }
            if (!askYesNo("Convert another? (y/n): ")) {
                return;
            }
        }
    }

    private Double promptValue() {
        while (true) {
            String line = readLine("Enter temperature value (or 'back'): ");
            if (line == null || line.trim().equalsIgnoreCase("back")) {
                return null;
            }
            try {
                return TemperatureInputParser.parse(line);
            } catch (InvalidTemperatureException ex) {
                out.println("Error: " + ex.getMessage());
            }
        }
    }

    private TemperatureUnit promptUnit() {
        while (true) {
            String line = readLine("Enter unit - C, F or K (or 'back'): ");
            if (line == null || line.trim().equalsIgnoreCase("back")) {
                return null;
            }
            Optional<TemperatureUnit> unit = parseUnit(line);
            if (unit.isPresent()) {
                return unit.get();
            }
            out.println("Error: Unknown unit. Use C, F or K (or Celsius, Fahrenheit, Kelvin).");
        }
    }

    private Optional<TemperatureUnit> parseUnit(String text) {
        String input = text.trim().toLowerCase(Locale.ROOT);
        for (TemperatureUnit unit : TemperatureUnit.values()) {
            String fullName = unit.getDisplayName().toLowerCase(Locale.ROOT);
            String letter = fullName.substring(0, 1);
            if (input.equals(fullName) || input.equals(letter)) {
                return Optional.of(unit);
            }
        }
        return Optional.empty();
    }

    private boolean askYesNo(String prompt) {
        while (true) {
            String line = readLine(prompt);
            if (line == null) {
                return false;
            }
            String answer = line.trim().toLowerCase(Locale.ROOT);
            if (answer.equals("y") || answer.equals("yes")) {
                return true;
            }
            if (answer.equals("n") || answer.equals("no")) {
                return false;
            }
            out.println("Please answer y or n.");
        }
    }

    private void showHistory() {
        if (history.isEmpty()) {
            out.println("No conversions yet.");
            return;
        }
        out.println("History (newest first):");
        int number = 1;
        for (ConversionResult result : history.getAll()) {
            out.println("  " + number++ + ". " + ResultFormatter.summary(result));
        }
    }

    /** Returns the next input line, or null when the input has ended. */
    private String readLine(String prompt) {
        out.print(prompt);
        out.flush();
        return in.hasNextLine() ? in.nextLine() : null;
    }
}x