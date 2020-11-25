package com.ns.chemcomp.service;

public class NoOrdersOnDateException extends Exception {

    public NoOrdersOnDateException(String message) {
        super(message);
    }

    public NoOrdersOnDateException(String message, Throwable cause) {
        super(message, cause);
    }
}
