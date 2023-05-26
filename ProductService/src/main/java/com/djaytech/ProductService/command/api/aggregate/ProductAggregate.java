package com.djaytech.ProductService.command.api.aggregate;

import com.djaytech.ProductService.command.api.commands.CreateProductCommand;
import com.djaytech.ProductService.command.api.commands.DeleteProductCommand;
import com.djaytech.ProductService.command.api.commands.UpdateProductCommand;
import com.djaytech.ProductService.command.api.events.ProductCreatedEvent;
import com.djaytech.ProductService.command.api.events.ProductDeletedEvent;
import com.djaytech.ProductService.command.api.events.ProductUpdatedEvent;
import com.djaytech.ProductService.exception.product.DeletedProductException;
import com.djaytech.ProductService.exception.product.EmptyProductNameException;
import com.djaytech.ProductService.exception.product.InvalidPriceException;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;

@Aggregate
public class ProductAggregate {
    @AggregateIdentifier
    private String productId;
    private String name;
    private BigDecimal price;
    private Integer quantity;
    private boolean deleted;

    @CommandHandler
    public ProductAggregate(CreateProductCommand createProductCommand){
        //adding few validations

        if(createProductCommand.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidPriceException();
        }

        if(createProductCommand.getName() == null
                || createProductCommand.getName().isBlank()) {
            throw new EmptyProductNameException();
        }

        ProductCreatedEvent productCreatedEvent =
                new ProductCreatedEvent();

        BeanUtils.copyProperties(createProductCommand,productCreatedEvent);

        AggregateLifecycle.apply(productCreatedEvent);
    }

    // Command Handler for DeleteProductCommand
    @CommandHandler
    public void handle(DeleteProductCommand deleteProductCommand) {
        // Perform any required validations

        ProductDeletedEvent productDeletedEvent = new ProductDeletedEvent(deleteProductCommand.getProductId());
        AggregateLifecycle.apply(productDeletedEvent);
    }

    @CommandHandler
    public void handle(UpdateProductCommand updateProductCommand) {
        // Perform any required validations

        if (deleted) {
            throw new DeletedProductException();
        }

        ProductUpdatedEvent productUpdatedEvent = new ProductUpdatedEvent(
                updateProductCommand.getProductId(),
                updateProductCommand.getName(),
                updateProductCommand.getPrice(),
                updateProductCommand.getQuantity()
        );
        AggregateLifecycle.apply(productUpdatedEvent);
    }

    public ProductAggregate(){

    }

    @EventSourcingHandler
    public void on(ProductCreatedEvent productCreatedEvent) {
        this.quantity = productCreatedEvent.getQuantity();
        this.productId = productCreatedEvent.getProductId();
        this.price = productCreatedEvent.getPrice();
        this.name = productCreatedEvent.getName();
    }

    @EventSourcingHandler
    public void on(ProductDeletedEvent productDeletedEvent) {
        this.deleted = true;
    }

    @EventSourcingHandler
    public void on(ProductUpdatedEvent productUpdatedEvent) {
        this.name = productUpdatedEvent.getName();
        this.price = productUpdatedEvent.getPrice();
        this.quantity = productUpdatedEvent.getQuantity();
    }
 }
