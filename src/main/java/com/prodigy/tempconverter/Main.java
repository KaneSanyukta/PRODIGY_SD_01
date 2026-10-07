package com.prodigy.tempconverter;

import com.prodigy.tempconverter.service.TemperatureConverter;
import com.prodigy.tempconverter.ui.gui.ConverterFrame;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ConverterFrame frame = new ConverterFrame(new TemperatureConverter());
            frame.setVisible(true);
        });
    }
}