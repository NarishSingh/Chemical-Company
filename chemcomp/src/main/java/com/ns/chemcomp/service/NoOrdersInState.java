package com.ns.chemcomp.service;

public class NoOrdersInState extends Exception {

    public NoOrdersInState(String message) {
        super(message);
    }

    public NoOrdersInState(String message, Throwable cause) {
        super(message, cause);
    }
}
