package com.djaytech.ProductService.command.api.events;

import com.djaytech.ProductService.shared.data.ProductData;
import com.djaytech.ProductService.shared.data.ProductRepository;
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
        ProductData product = new ProductData();

        BeanUtils.copyProperties(event, product);
        productRepository.save(product);
    }
}
