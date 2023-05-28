package com.djaytech.OrderService.commands.api.aggregate;

import com.djaytech.OrderService.commands.api.commands.CreateOrderCommand;
import com.djaytech.OrderService.commands.api.events.OrderCreatedEvent;
import com.djaytech.OrderService.commands.api.model.OrderStatus;
import jakarta.persistence.criteria.Order;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;

@Aggregate
public class OrderAggregate {

    @AggregateIdentifier
    private String orderId;
    private String productId;
    private String userId;
    private int quantity;
    private String addressId;
    private OrderStatus orderStatus;

    public OrderAggregate(){

    }

    @CommandHandler
    public OrderAggregate(CreateOrderCommand createOrderCommand) throws Exception{
        if(createOrderCommand.getQuantity() <= 0) {
            throw new IllegalArgumentException("Quantity cannot be lower than 1");
        }

        if(createOrderCommand.getQuantity() >=5) {
            throw new IllegalArgumentException("Quantity cannot be larger than 5");
        }

        if(createOrderCommand.getAddressId() == null
                || createOrderCommand.getAddressId().isBlank()) {
            throw new IllegalArgumentException("Order addressId is a required field");
        }

        OrderCreatedEvent orderCreatedEvent =
                new OrderCreatedEvent();

        BeanUtils.copyProperties(createOrderCommand,orderCreatedEvent);

        AggregateLifecycle.apply(orderCreatedEvent);

    }

    @EventSourcingHandler
    public void on(OrderCreatedEvent orderCreatedEvent) throws Exception {
        this.orderId = orderCreatedEvent.getOrderId();
        this.productId = orderCreatedEvent.getProductId();
        this.userId = orderCreatedEvent.getUserId();
        this.addressId = orderCreatedEvent.getAddressId();
        this.quantity = orderCreatedEvent.getQuantity();
        this.orderStatus = orderCreatedEvent.getOrderStatus();
    }

}
