package com.djaytech.ProductService.query.api.projection;

import com.djaytech.ProductService.query.api.queries.GetProductByIdQuery;
import com.djaytech.ProductService.query.api.queries.GetProductsByRangeQuery;
import com.djaytech.ProductService.query.api.queries.GetProductsQuery;
import com.djaytech.ProductService.shared.model.Product;
import com.djaytech.ProductService.shared.data.ProductData;
import com.djaytech.ProductService.shared.data.ProductRepository;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
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

    @QueryHandler
    public Product handle(GetProductByIdQuery getProductByIdQuery) {
        Optional<ProductData> optionalProduct = productRepository.findById(getProductByIdQuery.getProductId());

        if (optionalProduct.isEmpty()) {
            return null; // or throw an exception, or handle the case as needed
        }

        ProductData product = optionalProduct.get();

        return Product.builder()
                .quantity(product.getQuantity())
                .price(product.getPrice())
                .name(product.getName())
                .build();
    }

    @QueryHandler
    public List<Product> handle(GetProductsByRangeQuery getProductsByRangeQuery) {
        List<ProductData> products = productRepository.findByPriceBetween(getProductsByRangeQuery.getMin(), getProductsByRangeQuery.getMax());

        return products.stream()
                .map(productData -> Product.builder()
                        .quantity(productData.getQuantity())
                        .price(productData.getPrice())
                        .name(productData.getName())
                        .build())
                .collect(Collectors.toList());
    }


}
