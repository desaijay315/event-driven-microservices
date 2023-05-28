package com.djaytech.ProductService.query.api.controller;

import com.djaytech.ProductService.query.api.queries.GetProductByIdQuery;
import com.djaytech.ProductService.query.api.queries.GetProductsByRangeQuery;
import com.djaytech.ProductService.query.api.queries.GetProductsQuery;
import com.djaytech.ProductService.core.model.Product;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductQueryController {

    private QueryGateway queryGateway;

    public ProductQueryController(QueryGateway queryGateway) {
        this.queryGateway = queryGateway;
    }

    @GetMapping("/all")
    public List<Product> getAllProducts() {
        GetProductsQuery getProductsQuery =
                new GetProductsQuery();

        return queryGateway.query(getProductsQuery,
                        ResponseTypes.multipleInstancesOf(Product.class))
                .join();
    }

    @GetMapping("/{productId}")
    public Product getProductById(@PathVariable String productId) {
        GetProductByIdQuery getProductByIdQuery =
                new GetProductByIdQuery(productId);

        return queryGateway.query(getProductByIdQuery,
                        ResponseTypes.instanceOf(Product.class))
                .join();
    }

    @GetMapping("/range")
    public List<Product> getProductsByRange(@RequestParam BigDecimal min, @RequestParam BigDecimal max) {
        if (max.compareTo(min) < 0) {
            throw new IllegalArgumentException("Invalid range: max cannot be less than min");
        }

        GetProductsByRangeQuery getProductsByRangeQuery =
                new GetProductsByRangeQuery(min, max);

        return queryGateway.query(getProductsByRangeQuery,
                        ResponseTypes.multipleInstancesOf(Product.class))
                .join();
    }

}