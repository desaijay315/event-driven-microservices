package com.djaytech.ProductService.query.api.queries;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class GetProductsByRangeQuery {
    private BigDecimal min;
    private BigDecimal max;
}
