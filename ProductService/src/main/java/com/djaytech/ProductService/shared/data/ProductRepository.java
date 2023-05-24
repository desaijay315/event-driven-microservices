package com.djaytech.ProductService.shared.data;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<ProductData, String> {

}
