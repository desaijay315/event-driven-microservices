package com.djaytech.ProductService.shared.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class Product {

    @NotBlank(message="Product name is a required field")
    private String name;

    @Min(value=1, message="Price cannot be lower than 1")
    private BigDecimal price;

    @Min(value=1, message="Quantity cannot be lower than 1")
    @Max(value=5, message="Quantity cannot be larger than 5")
    private Integer quantity;
}
