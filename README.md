# Axon Framework Sample Project

This is a sample project showcasing the usage of the Axon Framework. The project includes several components such as commands, aggregates, controllers, and event handlers to demonstrate the basic functionality of the Axon Framework.

## Axon Framework Introduction

Axon Framework is an open-source Java framework that provides a flexible and scalable approach to building event-driven applications. It provides the necessary building blocks for implementing the Command Query Responsibility Segregation (CQRS) and Event Sourcing patterns. The framework simplifies the development of distributed systems by providing abstractions and utilities for handling commands, events, aggregates, and event sourcing.

### CreateProductCommand

```java
package com.djaytech.ProductService.command.api.commands;

import lombok.Builder;
import lombok.Data;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.math.BigDecimal;

@Data
@Builder
public class CreateProductCommand {

    @TargetAggregateIdentifier
    private String productId;
    private String name;
    private BigDecimal price;
    private Integer quantity;
}
```

The `CreateProductCommand` class represents a command for creating a new product. It contains the necessary fields such as `productId`, `name`, `price`, and `quantity`. The `@TargetAggregateIdentifier` annotation marks the `productId` field as the identifier for the target aggregate.

### ProductAggregate

```java
package com.djaytech.ProductService.command.api.aggregate;

import com.djaytech.ProductService.command.api.commands.CreateProductCommand;
import com.djaytech.ProductService.command.api.events.ProductCreatedEvent;
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

    @CommandHandler
    public ProductAggregate(CreateProductCommand createProductCommand){
        //you can perform all the validations here
        ProductCreatedEvent productCreatedEvent =
                new ProductCreatedEvent();

        BeanUtils.copyProperties(createProductCommand,productCreatedEvent);

        AggregateLifecycle.apply(productCreatedEvent);
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
}
```

The `ProductAggregate` class represents an aggregate that handles the `CreateProductCommand` and produces the `ProductCreatedEvent`. It is annotated with `@Aggregate` to mark it as an Axon aggregate.

- The `@AggregateIdentifier` annotation identifies the `productId` field as the aggregate identifier.
- The `@CommandHandler` annotation indicates that the constructor with the `CreateProductCommand` parameter is responsible for handling the command.
- Inside the command handler, a `ProductCreatedEvent` is created, and the properties of the command are copied to the event using `BeanUtils.copyProperties()`.
- The `AggregateLifecycle.apply()` method is called to apply the event to the aggregate, triggering the event sourcing mechanism.
- The `@EventSourcingHandler` annotation marks the `on()` method as the event handler for `ProductCreatedEvent`. Inside this method, the aggregate's state is updated based on the event data.

### ProductCommandController

```java
package com.djaytech.ProductService.command.api.controller;

import com.djaytech.ProductService.command

.api.commands.CreateProductCommand;
import com.djaytech.ProductService.command.api.model.Product;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/product")
public class ProductCommandController {

    @Autowired
    private CommandGateway commandGateway;

    @PostMapping
    public String addProduct(@RequestBody Product product){
        CreateProductCommand createProductCommand = CreateProductCommand.builder()
                .productId(UUID.randomUUID().toString())
                .name(product.getName())
                .price(product.getPrice())
                .quantity(product.getQuantity())
                .build();
        return commandGateway.sendAndWait(createProductCommand);
    }
}
```

The `ProductCommandController` class represents a REST controller for handling product-related commands.

- The `@RestController` annotation indicates that this class is a REST controller.
- The `@RequestMapping("/product")` annotation maps the controller to the `/product` endpoint.
- The `CommandGateway` is injected using the `@Autowired` annotation, which allows sending commands to the Axon Framework.
- The `addProduct()` method handles the HTTP POST request for adding a new product.
- Inside the method, a `CreateProductCommand` is created using the provided `Product` object, and a unique `productId` is generated using `UUID.randomUUID().toString()`.
- The command is sent using the `commandGateway.sendAndWait()` method, which waits for the command execution to complete and returns a response.

### ProductEventsHandler

```java
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
```

The `ProductEventsHandler` class represents an event handler for the `ProductCreatedEvent`. It is responsible for persisting the product data to the database.

- The `@Component` annotation marks this class as a Spring component to be automatically detected and instantiated.
- The `ProductRepository` is injected using the `@Autowired` annotation, allowing access to the database operations.
- The `@EventHandler` annotation indicates that the `on()` method handles the `ProductCreatedEvent`.
- Inside the event handler, a new `Product` object is created and its properties are copied from the event using `BeanUtils.copyProperties()`.
- The product is then saved to the database using the `productRepository.save()` method.


### `CustomExceptionHandler` class:

```java
package com.djaytech.ProductService.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class CustomExceptionHandler {
```
- This code declares a class named `CustomExceptionHandler` in the package `com.djaytech.ProductService.exception`. It is annotated with `@ControllerAdvice`, indicating that it provides centralized exception handling for the controllers in the application.

```java
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        BindingResult bindingResult = ex.getBindingResult();
        Map<String, String> errorMessage = new HashMap<>();

        bindingResult.getFieldErrors().forEach(fieldError ->
                errorMessage.put(fieldError.getField(), fieldError.getDefaultMessage())
        );
```
- This method handles exceptions of type `MethodArgumentNotValidException`, which occurs when validation fails for a request parameter or request body object. It takes the exception `ex` as a parameter.
- The method obtains the `BindingResult` from the exception to retrieve information about the validation errors.
- It creates a `Map<String, String>` named `errorMessage` to store the field names and their corresponding error messages.
- Using `bindingResult.getFieldErrors().forEach`, it iterates over the field errors and populates the `errorMessage` map with field names as keys and error messages as values.

```java
        ErrorResponse errorResponse = ErrorResponse.builder()
                .errorMessage(errorMessage)
                .errorCode("VALIDATION_ERROR")
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }
```
- This code constructs an `ErrorResponse` object using a builder pattern.
- The `errorMessage` field of the `ErrorResponse` is set to the `errorMessage` map obtained from the validation errors.
- The `errorCode` field is set to `"VALIDATION_ERROR"`.
- Finally, a `ResponseEntity` is created with the `errorResponse` object and an HTTP status of `BAD_REQUEST`, indicating a client error. The response entity is returned.

```java
    @ExceptionHandler(DeletedProductException.class)
    public ResponseEntity<ErrorResponse> handleDeletedProductException(DeletedProductException ex) {
        Map<String, String> errorMessage = new HashMap<>();
        errorMessage.put("error", ex.getMessage());

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setErrorMessage(errorMessage);
        errorResponse.setErrorCode("DELETED_PRODUCT_ERROR");

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }
```
- This method handles exceptions of type `DeletedProductException`, which represents an exception specific to deleted products. It takes the exception `ex` as a parameter.
- It creates a `Map<String, String>` named `errorMessage` and puts the error message from the exception into it.
- An `ErrorResponse` object is created, and its `errorMessage` field is set to the `errorMessage` map, and `errorCode` is set to `"DELETED_PRODUCT_ERROR"`.
- A `ResponseEntity` is created with the `errorResponse` object and an HTTP status of `BAD_REQUEST`, indicating a client error. The response entity is returned.