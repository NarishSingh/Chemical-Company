package com.ns.chemcomp.service;

public class NoOrdersForUser extends Exception {

    public NoOrdersForUser(String message) {
        super(message);
    }

    public NoOrdersForUser(String message, Throwable cause) {
        super(message, cause);
    }
}
