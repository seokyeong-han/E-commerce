package com.example.ecommerce.product.infrastructure;


import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaProductRepository extends JpaRepository<ProductJpaEntity, Long> {
}
