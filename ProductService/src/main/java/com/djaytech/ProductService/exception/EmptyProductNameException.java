package com.djaytech.ProductService.exception;

public class EmptyProductNameException extends IllegalArgumentException {
    public EmptyProductNameException() {
        super("Product name cannot be empty");
    }
}

