package com.djaytech.ProductService.query.api.controller;

import com.djaytech.ProductService.query.api.queries.GetProductsQuery;
import com.djaytech.ProductService.shared.model.Product;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductQueryController {

    private QueryGateway queryGateway;

    public ProductQueryController(QueryGateway queryGateway) {
        this.queryGateway = queryGateway;
    }

    @GetMapping
    public List<Product> getAllProducts() {
        GetProductsQuery getProductsQuery =
                new GetProductsQuery();

        return queryGateway.query(getProductsQuery,
                        ResponseTypes.multipleInstancesOf(Product.class))
                .join();
    }
}