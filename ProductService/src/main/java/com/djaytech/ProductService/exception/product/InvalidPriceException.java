package com.djaytech.ProductService.exception.product;

public class InvalidPriceException extends IllegalArgumentException {
    public InvalidPriceException() {
        super("Price cannot be less or equal than zero");
    }
}
