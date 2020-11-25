package com.ns.chemcomp.service;

public class NoOrdersForProduct extends Exception {

    public NoOrdersForProduct(String message) {
        super(message);
    }

    public NoOrdersForProduct(String message, Throwable cause) {
        super(message, cause);
    }
}
