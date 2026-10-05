package com.prodigy.tempconverter.model;

public enum TemperatureUnit {

    CELSIUS("Celsius", "\u00B0C") {
        @Override
        public double toKelvin(double value) {
            return value + 273.15;
        }

        @Override
        public double fromKelvin(double kelvin) {
            return kelvin - 273.15;
        }
    },

    FAHRENHEIT("Fahrenheit", "\u00B0F") {
        @Override
        public double toKelvin(double value) {
            return (value - 32.0) * 5.0 / 9.0 + 273.15;
        }

        @Override
        public double fromKelvin(double kelvin) {
            return (kelvin - 273.15) * 9.0 / 5.0 + 32.0;
        }
    },

    KELVIN("Kelvin", "K") {
        @Override
        public double toKelvin(double value) {
            return value;
        }

        @Override
        public double fromKelvin(double kelvin) {
            return kelvin;
        }
    };

    private final String displayName;
    private final String symbol;

    TemperatureUnit(String displayName, String symbol) {
        this.displayName = displayName;
        this.symbol = symbol;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getSymbol() {
        return symbol;
    }

    public abstract double toKelvin(double value);

    public abstract double fromKelvin(double kelvin);
}