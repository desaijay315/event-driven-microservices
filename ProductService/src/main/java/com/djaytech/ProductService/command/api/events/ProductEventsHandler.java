package com.djaytech.ProductService.command.api.events;

import com.djaytech.ProductService.shared.data.ProductData;
import com.djaytech.ProductService.shared.data.ProductRepository;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ProductEventsHandler {

    @Autowired
    private ProductRepository productRepository;

    @EventHandler
    public void on(ProductCreatedEvent event){
        ProductData product = new ProductData();

        BeanUtils.copyProperties(event, product);
        productRepository.save(product);
    }

    @EventHandler
    public void on(ProductDeletedEvent productDeletedEvent) {
        String productId = productDeletedEvent.getProductId();
        // Perform actions to handle the product deletion, such as updating the product's status or performing cleanup

        // For example, you can retrieve the product from the repository and update its status
        Optional<ProductData> productOptional = productRepository.findById(productId);
        if (productOptional.isPresent()) {
            ProductData product = productOptional.get();
            product.setDeleted(true);
            productRepository.save(product);
        }
    }

}
