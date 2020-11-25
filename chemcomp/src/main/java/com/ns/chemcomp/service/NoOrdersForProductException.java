package com.ns.chemcomp.service;

public class NoOrdersForProductException extends Exception {

    public NoOrdersForProductException(String message) {
        super(message);
    }

    public NoOrdersForProductException(String message, Throwable cause) {
        super(message, cause);
    }
}
