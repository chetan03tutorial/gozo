package com.lbg.ib.api.sales.shared.exception;

public class InvalidFormatException extends RuntimeException {
    /**
     * Serial Version.
     */
    private static final long serialVersionUID = 1121115024617084116L;

    public InvalidFormatException(String message) {
        super(message);
    }

    public InvalidFormatException(String message, Exception ex) {
        super(message, ex);
    }
}