package com.djaytech.ProductService.command.api.commands;

import lombok.Builder;
import lombok.Data;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Data
@Builder
public class DeleteProductCommand {

    @TargetAggregateIdentifier
    private String productId;

    public DeleteProductCommand(String productId) {
        this.productId = productId;
    }
}
