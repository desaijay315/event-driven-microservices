package com.djaytech.ProductService.command.api.events;

import com.djaytech.ProductService.core.data.ProductData;
import com.djaytech.ProductService.core.data.ProductRepository;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.messaging.interceptors.ExceptionHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
@ProcessingGroup("product-group")
public class ProductEventsHandler {

    @Autowired
    private ProductRepository productRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductEventsHandler.class);

    //common errors of this class
    @ExceptionHandler(resultType=Exception.class)
    public void handle(Exception exception) throws Exception {
        throw exception;
    }

    //IllegalArgumentException handled
    @ExceptionHandler(resultType=IllegalArgumentException.class)
    public void handle(IllegalArgumentException exception) {
        // Log error message
    }
    @EventHandler
    public void on(ProductCreatedEvent event){
        ProductData product = new ProductData();

        BeanUtils.copyProperties(event, product);

        try {
            productRepository.save(product);
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
        }

    }

    @EventHandler
    public void on(ProductDeletedEvent productDeletedEvent) {
        String productId = productDeletedEvent.getProductId();
        // Perform actions to handle the product deletion, such as updating the product's status or performing cleanup

        // For example, you can retrieve the product from the repository and update its status
        Optional<ProductData> productOptional = productRepository.findById(productId);

        LOGGER.debug("ProductDeletedEvent: Deleted Product " + productId);

        if (productOptional.isPresent()) {
            ProductData product = productOptional.get();
            product.setDeleted(true);
            productRepository.save(product);
        }
    }

    @EventHandler
    public void on(ProductUpdatedEvent productUpdatedEvent) {
        String productId = productUpdatedEvent.getProductId();
        // Perform actions to handle the product update, such as updating the product's information
        LOGGER.debug("ProductUpdatedEvent: Update Product " + productId);

        // For example, you can retrieve the product from the repository and update its fields
        Optional<ProductData> productOptional = productRepository.findById(productId);
        if (productOptional.isPresent()) {
            ProductData product = productOptional.get();
            if (product.isDeleted()) { // Check if the product is already deleted
                throw new IllegalStateException("Cannot update a deleted product");
            }
            product.setName(productUpdatedEvent.getName());
            product.setPrice(productUpdatedEvent.getPrice());
            product.setQuantity(productUpdatedEvent.getQuantity());
            LOGGER.debug("ProductUpdatedEvent: Updated Product Data " + product);

            productRepository.save(product);
        }
    }


}
