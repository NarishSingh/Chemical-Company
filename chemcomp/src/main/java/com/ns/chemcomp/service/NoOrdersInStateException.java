package com.ns.chemcomp.service;

public class NoOrdersInStateException extends Exception {

    public NoOrdersInStateException(String message) {
        super(message);
    }

    public NoOrdersInStateException(String message, Throwable cause) {
        super(message, cause);
    }
}
