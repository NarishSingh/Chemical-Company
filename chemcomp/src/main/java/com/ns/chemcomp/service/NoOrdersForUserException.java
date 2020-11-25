package com.ns.chemcomp.service;

public class NoOrdersForUserException extends Exception {

    public NoOrdersForUserException(String message) {
        super(message);
    }

    public NoOrdersForUserException(String message, Throwable cause) {
        super(message, cause);
    }
}
