package com.djaytech.ProductService.exception.product;

public class EmptyProductNameException extends IllegalArgumentException {
    public EmptyProductNameException() {
        super("Product name cannot be empty");
    }
}

