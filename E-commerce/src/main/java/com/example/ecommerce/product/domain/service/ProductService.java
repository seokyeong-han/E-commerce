package com.example.ecommerce.product.domain.service;

import com.example.ecommerce.product.domain.model.Product;
import com.example.ecommerce.product.domain.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("상품이 존재하지 않습니다."));
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }
}
