package com.djaytech.ProductService.query.api.projection;

import com.djaytech.ProductService.query.api.queries.GetProductsQuery;
import com.djaytech.ProductService.shared.model.Product;
import com.djaytech.ProductService.shared.data.ProductData;
import com.djaytech.ProductService.shared.data.ProductRepository;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProductProjection {

    private ProductRepository productRepository;

    public ProductProjection(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @QueryHandler
    public List<Product> handle(GetProductsQuery getProductsQuery) {
        List<ProductData> products =
                productRepository.findAll();

        return products.stream()
                        .map(product -> Product
                                .builder()
                                .quantity(product.getQuantity())
                                .price(product.getPrice())
                                .name(product.getName())
                                .build())
                        .collect(Collectors.toList());
    }
}
