package com.prodigy.tempconverter.exception;

public class InvalidTemperatureException extends IllegalArgumentException {

    private static final long serialVersionUID = 1L;

    public InvalidTemperatureException(String message) {
        super(message);
    }
}