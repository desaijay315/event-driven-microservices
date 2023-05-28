package com.djaytech.ProductService.command.api.interceptors;

import com.djaytech.ProductService.command.api.commands.CreateProductCommand;
import com.djaytech.ProductService.exception.product.EmptyProductNameException;
import com.djaytech.ProductService.exception.product.InvalidPriceException;
import org.axonframework.commandhandling.CommandMessage;
import org.axonframework.messaging.MessageDispatchInterceptor;
import org.springframework.stereotype.Component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.List;
import java.util.function.BiFunction;

@Component
public class CreateProductCommandInterceptor implements MessageDispatchInterceptor<CommandMessage<?>> {

    private static final Logger LOGGER = LoggerFactory.getLogger(CreateProductCommandInterceptor.class);

    @Override
    public BiFunction<Integer, CommandMessage<?>, CommandMessage<?>> handle(List<? extends CommandMessage<?>> messages) {
        return (index, command) -> {
            LOGGER.info("Dispatching a command {}.", command.getPayloadType());
            if(CreateProductCommand.class.equals(command.getPayloadType())){
                CreateProductCommand createProductCommand = (CreateProductCommand) command.getPayload();
                if(createProductCommand.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
                    throw new InvalidPriceException();
                }

                if(createProductCommand.getName() == null
                        || createProductCommand.getName().isBlank()) {
                    throw new EmptyProductNameException();
                }
            }
            return command;
        };
    }
}
