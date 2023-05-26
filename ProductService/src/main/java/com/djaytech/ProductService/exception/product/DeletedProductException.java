package com.djaytech.ProductService.exception.product;

public class DeletedProductException extends RuntimeException {
    public DeletedProductException() {
        super("Cannot update a deleted product");
    }
}
