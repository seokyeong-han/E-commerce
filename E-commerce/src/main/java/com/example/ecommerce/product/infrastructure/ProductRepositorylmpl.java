package com.example.ecommerce.product.infrastructure;

import com.example.ecommerce.product.domain.model.Product;
import com.example.ecommerce.product.domain.repository.ProductRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ProductRepositorylmpl implements ProductRepository {
    private JpaProductRepository jpaRepository;

    public ProductRepositorylmpl(JpaProductRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public List<Product> findAll() {
        return List.of();
    }

    @Override
    public Optional<Product> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public Product save(Product product) {
        return null;
    }
}
