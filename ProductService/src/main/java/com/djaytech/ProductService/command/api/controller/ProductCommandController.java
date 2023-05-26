package com.djaytech.ProductService.command.api.controller;

import com.djaytech.ProductService.command.api.commands.CreateProductCommand;
import com.djaytech.ProductService.command.api.commands.DeleteProductCommand;
import com.djaytech.ProductService.command.api.commands.UpdateProductCommand;
import com.djaytech.ProductService.core.model.Product;
import jakarta.validation.Valid;
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
    public String  addProduct(@Valid @RequestBody Product product){
        CreateProductCommand createProductCommand = CreateProductCommand.builder()
                .productId(UUID.randomUUID().toString())
                .name(product.getName())
                .price(product.getPrice())
                .quantity(product.getQuantity())
                .build();

        String responseValue;
        try{
            responseValue = commandGateway.sendAndWait(createProductCommand);
        }catch(Exception ex){
            responseValue = ex.getLocalizedMessage();
        }
        return responseValue;
    }

    @DeleteMapping("/{productId}")
    public void deleteProduct(@PathVariable String productId) {
        DeleteProductCommand deleteProductCommand = DeleteProductCommand.builder()
                .productId(productId)
                .build();
        commandGateway.sendAndWait(deleteProductCommand);

        String responseValue;
        try{
            responseValue = commandGateway.sendAndWait(deleteProductCommand);
        }catch(Exception ex){
            responseValue = ex.getLocalizedMessage();
        }
    }

    @PutMapping("/{productId}")
    public void updateProduct(@PathVariable String productId, @RequestBody Product product) {
        UpdateProductCommand updateProductCommand = UpdateProductCommand.builder()
                .productId(productId)
                .name(product.getName())
                .price(product.getPrice())
                .quantity(product.getQuantity())
                .build();
        commandGateway.sendAndWait(updateProductCommand);
    }

}
