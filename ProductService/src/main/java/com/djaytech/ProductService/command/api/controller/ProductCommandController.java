package com.djaytech.ProductService.command.api.controller;

import com.djaytech.ProductService.command.api.commands.CreateProductCommand;
import com.djaytech.ProductService.command.api.commands.DeleteProductCommand;
import com.djaytech.ProductService.shared.model.Product;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/product")
public class ProductCommandController {

    @Autowired
    private CommandGateway commandGateway;
    @PostMapping
    public String  addProduct(@RequestBody Product product){
        CreateProductCommand createProductCommand = CreateProductCommand.builder()
                .productId(UUID.randomUUID().toString())
                .name(product.getName())
                .price(product.getPrice())
                .quantity(product.getQuantity())
                .build();
        return commandGateway.sendAndWait(createProductCommand);
    }

    @DeleteMapping("/{productId}")
    public void deleteProduct(@PathVariable String productId) {
        DeleteProductCommand deleteProductCommand = DeleteProductCommand.builder()
                .productId(productId)
                .build();
        commandGateway.sendAndWait(deleteProductCommand);
    }

}
