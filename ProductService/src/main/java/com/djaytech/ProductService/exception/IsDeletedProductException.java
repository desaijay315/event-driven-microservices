package com.djaytech.ProductService.exception;

public class IsDeletedProductException extends RuntimeException {

    public IsDeletedProductException() {
        super("This product is already deleted");
    }
}
