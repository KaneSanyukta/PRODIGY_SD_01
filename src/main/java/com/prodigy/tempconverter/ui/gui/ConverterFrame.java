package com.prodigy.tempconverter.ui.gui;

import com.prodigy.tempconverter.exception.InvalidTemperatureException;
import com.prodigy.tempconverter.history.ConversionHistory;
import com.prodigy.tempconverter.model.ConversionResult;
import com.prodigy.tempconverter.model.TemperatureUnit;
import com.prodigy.tempconverter.service.TemperatureConverter;
import com.prodigy.tempconverter.service.TemperatureInputParser;
import com.prodigy.tempconverter.ui.ResultFormatter;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
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
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ConverterFrame extends JFrame {

    private static final String QUICK_REFERENCE =
            "<html>Water freezes at 0 \u00B0C = 32 \u00B0F = 273.15 K<br>"
            + "Water boils at 100 \u00B0C = 212 \u00B0F = 373.15 K</html>";

    private final TemperatureConverter converter;
    private final ConversionHistory history;

    private final JTextField valueField = new JTextField(10);
    private final JComboBox<TemperatureUnit> unitBox = new JComboBox<>(TemperatureUnit.values());
    private final JButton convertButton = new JButton("Convert");
    private final JButton clearButton = new JButton("Clear");
    private final JLabel messageLabel = new JLabel(" ");
    private final JTextArea resultArea = new JTextArea(4, 25);

    private final DefaultListModel<String> historyModel = new DefaultListModel<>();
    private final JList<String> historyList = new JList<>(historyModel);
    private final JButton clearHistoryButton = new JButton("Clear history");

    public ConverterFrame(TemperatureConverter converter, ConversionHistory history) {
        super("Temperature Converter");
        this.converter = converter;
        this.history = history;

        buildUi();
        registerListeners();
        refreshHistory();

        pack();
        setMinimumSize(getSize());
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                valueField.requestFocusInWindow();
            }
        });
    }

    private void buildUi() {
        valueField.setToolTipText("Enter a number, for example 25 or -4.5");

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

        historyList.setVisibleRowCount(6);
        historyList.setPrototypeCellValue("-9999.99 \u00B0C  \u2192  -9999.99 \u00B0F | -9999.99 K");

        JPanel historyPanel = new JPanel(new BorderLayout(0, 5));
        historyPanel.setBorder(BorderFactory.createTitledBorder("History (this session)"));
        historyPanel.add(new JScrollPane(historyList), BorderLayout.CENTER);
        historyPanel.add(clearHistoryButton, BorderLayout.SOUTH);

        JPanel center = new JPanel(new GridLayout(2, 1, 0, 10));
        center.add(new JScrollPane(resultArea));
        center.add(historyPanel);

        JLabel reference = new JLabel(QUICK_REFERENCE);
        reference.setFont(reference.getFont().deriveFont(Font.PLAIN, 12f));
        reference.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));

        JPanel content = new JPanel(new BorderLayout(0, 10));
        content.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        content.add(top, BorderLayout.NORTH);
        content.add(center, BorderLayout.CENTER);
        content.add(reference, BorderLayout.SOUTH);

        setContentPane(content);
        getRootPane().setDefaultButton(convertButton);
    }

    private void registerListeners() {
        convertButton.addActionListener(e -> onConvert());
        clearButton.addActionListener(e -> onClear());
        clearHistoryButton.addActionListener(e -> onClearHistory());
    }

    private void onConvert() {
        messageLabel.setText(" ");
        try {
            double value = TemperatureInputParser.parse(valueField.getText());
            TemperatureUnit unit = (TemperatureUnit) unitBox.getSelectedItem();
            ConversionResult result = converter.convert(value, unit);
            history.add(result);
            resultArea.setText(ResultFormatter.details(result));
            refreshHistory();
        } catch (InvalidTemperatureException ex) {
            showError(ex.getMessage());
        }
        valueField.selectAll();
        valueField.requestFocusInWindow();
    }

    private void refreshHistory() {
        historyModel.clear();
        history.getAll().forEach(r -> historyModel.addElement(ResultFormatter.summary(r)));
        clearHistoryButton.setEnabled(!history.isEmpty());
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

    private void onClearHistory() {
        history.clear();
        refreshHistory();
    }
}