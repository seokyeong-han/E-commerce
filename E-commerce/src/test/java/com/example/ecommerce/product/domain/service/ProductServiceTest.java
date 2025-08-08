package com.example.ecommerce.product.domain.service;

import com.example.ecommerce.product.domain.model.Product;
import com.example.ecommerce.product.domain.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class ProductServiceTest {
    private ProductService productService;
    private ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        productRepository = mock(ProductRepository.class);
        productService = new ProductService(productRepository);
    }

    @Test
    void 상품_id_조회_성공(){
        // given
        Product product = new Product(1L, "상품1", 1000L, 100, LocalDateTime.now());
        given(productRepository.findById(1L)).willReturn(Optional.of(product));
        //productRepository.findById(1L)이 호출되면 → Optional.of(product)를 리턴

        // when
        Product found = productService.getProductById(1L);

        // then
        assertEquals(1L, found.getId());
        assertEquals("상품1", found.getName());
    }

    @Test
    void 상품조회_실패(){
        // given
        given(productRepository.findById(999L)).willReturn(Optional.empty());
        // then
        assertThrows(IllegalArgumentException.class, () -> {
            productService.getProductById(999L);
        });
    }

    @Test
    void 전체_상품_조회() {
        // given
        List<Product> mockList = List.of(
                new Product(1L, "상품1", 1000L, 10, LocalDateTime.now()),
                new Product(2L, "상품2", 2000L, 5, LocalDateTime.now())
        );
        given(productRepository.findAll()).willReturn(mockList);
        // when
        List<Product> result = productService.getAllProducts();
        // then
        assertEquals(2, result.size());
        assertEquals("상품1", result.get(0).getName());
        assertEquals("상품2", result.get(1).getName());
    }



}