package com.djaytech.OrderService.commands.api.controller;

import com.djaytech.OrderService.commands.api.commands.CreateOrderCommand;
import com.djaytech.OrderService.commands.api.model.OrderStatus;
import com.djaytech.OrderService.core.data.OrderData;
import jakarta.validation.Valid;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private CommandGateway commandGateway;

    @PostMapping("/create-order")
    public String  createOrder(@Valid @RequestBody OrderData orderData){

        String userId = "27b95829-4f3f-4ddf-8983-151ba010e35b";
        String orderId = UUID.randomUUID().toString();

        CreateOrderCommand createOrderCommand = CreateOrderCommand.builder().addressId(orderData.getAddressId())
                .productId(orderData.getProductId()).userId(userId).quantity(orderData.getQuantity()).orderId(orderId)
                .orderStatus(OrderStatus.CREATED).build();

        String responseValue;
        try{
            responseValue = commandGateway.sendAndWait(createOrderCommand);
        }catch(Exception ex){
            responseValue = ex.getLocalizedMessage();
        }
        return responseValue;
    }
}
