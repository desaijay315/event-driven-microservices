package com.djaytech.ProductService.exception;

public class InvalidPriceException extends IllegalArgumentException {
    public InvalidPriceException() {
        super("Price cannot be less or equal than zero");
    }
}
