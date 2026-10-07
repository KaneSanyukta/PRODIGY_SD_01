package com.prodigy.tempconverter.ui.gui;

import com.prodigy.tempconverter.model.ConversionResult;
import com.prodigy.tempconverter.model.TemperatureUnit;
import com.prodigy.tempconverter.service.TemperatureConverter;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;

public class ConverterFrame extends JFrame {

    private final TemperatureConverter converter;

    private final JTextField valueField = new JTextField(10);
    private final JComboBox<TemperatureUnit> unitBox = new JComboBox<>(TemperatureUnit.values());
    private final JButton convertButton = new JButton("Convert");
    private final JButton clearButton = new JButton("Clear");
    private final JLabel messageLabel = new JLabel(" ");
    private final JTextArea resultArea = new JTextArea(4, 25);

    public ConverterFrame(TemperatureConverter converter) {
        super("Temperature Converter");
        this.converter = converter;

        buildUi();
        registerListeners();

        pack();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    private void buildUi() {
        JPanel form = new JPanel(new GridLayout(2, 2, 8, 8));
        form.add(new JLabel("Temperature value:"));
        form.add(valueField);
        form.add(new JLabel("Original unit:"));
        form.add(unitBox);

        unitBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                          boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof TemperatureUnit unit) {
                    setText(unit.getDisplayName() + " (" + unit.getSymbol() + ")");
                }
                return this;
            }
        });

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttons.add(convertButton);
        buttons.add(clearButton);

        messageLabel.setForeground(Color.RED);

        resultArea.setEditable(false);
        resultArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));

        JPanel top = new JPanel(new BorderLayout(0, 10));
        top.add(form, BorderLayout.NORTH);
        top.add(buttons, BorderLayout.CENTER);
        top.add(messageLabel, BorderLayout.SOUTH);

        JPanel content = new JPanel(new BorderLayout(0, 10));
        content.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        content.add(top, BorderLayout.NORTH);
        content.add(new JScrollPane(resultArea), BorderLayout.CENTER);

        setContentPane(content);
        getRootPane().setDefaultButton(convertButton); // Enter key triggers Convert
    }

    private void registerListeners() {
        convertButton.addActionListener(e -> onConvert());
        clearButton.addActionListener(e -> onClear());
    }

    private void onConvert() {
        messageLabel.setText(" ");

        String text = valueField.getText().trim();
        if (text.isEmpty()) {
            showError("Please enter a temperature value.");
            return;
        }

        double value;
        try {
            value = Double.parseDouble(text);
        } catch (NumberFormatException ex) {
            showError("Invalid number. Example: 25 or -4.5");
            return;
        }

        TemperatureUnit unit = (TemperatureUnit) unitBox.getSelectedItem();
        ConversionResult result = converter.convert(value, unit);
        showResult(result);
    }

    private void showResult(ConversionResult result) {
        StringBuilder sb = new StringBuilder();
        sb.append(formatLine("Input", result.inputValue(), result.inputUnit()));
        result.convertedValues().forEach((unit, converted) ->
                sb.append(formatLine(unit.getDisplayName(), converted, unit)));
        resultArea.setText(sb.toString());
    }

    private String formatLine(String label, double value, TemperatureUnit unit) {
        return String.format("%-11s: %.2f %s%n", label, value, unit.getSymbol());
    }

    private void showError(String message) {
        messageLabel.setText(message);
        resultArea.setText("");
    }

    private void onClear() {
        valueField.setText("");
        unitBox.setSelectedIndex(0);
        resultArea.setText("");
        messageLabel.setText(" ");
        valueField.requestFocusInWindow();
    }
}