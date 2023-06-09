package com.djaytech.ProductService.core.data;

import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.List;

public interface ProductRepository extends JpaRepository<ProductData, String> {

    List<ProductData> findByPriceBetween(BigDecimal min, BigDecimal max);
}
