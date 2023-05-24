package com.djaytech.ProductService.command.api.events;

import com.djaytech.ProductService.command.api.data.Product;
import com.djaytech.ProductService.command.api.data.ProductRepository;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProductEventsHandler {

    @Autowired
    private ProductRepository productRepository;

    @EventHandler
    public void on(ProductCreatedEvent event){
        Product product = new Product();

        BeanUtils.copyProperties(event, product);
        productRepository.save(product);
    }
}
