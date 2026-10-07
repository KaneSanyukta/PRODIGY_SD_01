package com.prodigy.tempconverter;

import com.prodigy.tempconverter.history.ConversionHistory;
import com.prodigy.tempconverter.service.TemperatureConverter;
import com.prodigy.tempconverter.ui.console.ConsoleApp;
import com.prodigy.tempconverter.ui.gui.ConverterFrame;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {
        TemperatureConverter converter = new TemperatureConverter();
        ConversionHistory history = new ConversionHistory();

        if (args.length > 0 && args[0].equals("--console")) {
            try (Scanner scanner = new Scanner(System.in)) {
                new ConsoleApp(converter, history, scanner, System.out).run();
            }
            return;
        }

        SwingUtilities.invokeLater(() -> {
            useSystemLookAndFeel();
            new ConverterFrame(converter, history).setVisible(true);
        });
    }

    private static void useSystemLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ReflectiveOperationException | UnsupportedLookAndFeelException ex) {
            LOGGER.log(Level.WARNING, "System look and feel unavailable, using default", ex);
        }
    }
}