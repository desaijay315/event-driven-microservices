package com.djaytech.ProductService.exception;

public class DeletedProductException extends RuntimeException {
    public DeletedProductException() {
        super("Cannot update a deleted product");
    }
}
