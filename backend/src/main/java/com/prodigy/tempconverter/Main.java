package com.prodigy.tempconverter;

import com.prodigy.tempconverter.history.ConversionHistory;
import com.prodigy.tempconverter.service.TemperatureConverter;
import com.prodigy.tempconverter.ui.console.ConsoleApp;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            new ConsoleApp(new TemperatureConverter(), new ConversionHistory(), scanner, System.out).run();
        }
    }
}