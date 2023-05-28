package com.djaytech.OrderService.commands.api.events;

import com.djaytech.OrderService.core.data.OrderData;
import com.djaytech.OrderService.core.data.OrderRepository;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.messaging.interceptors.ExceptionHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
@ProcessingGroup("order-group")
public class OrderEventsHandler {

    @Autowired
    private OrderRepository orderRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderEventsHandler.class);

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
    public void on(OrderCreatedEvent event){
        OrderData order = new OrderData();

        BeanUtils.copyProperties(event, order);

        try {
            orderRepository.save(order);
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
        }
    }


}
