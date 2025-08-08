package com.example.ecommerce.product.application.facade;

import com.example.ecommerce.product.application.dto.ProductResponse;
import com.example.ecommerce.product.domain.model.Product;
import com.example.ecommerce.product.domain.service.ProductService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Component
public class ProductFacade {
    private final ProductService productService;

    public ProductFacade(ProductService productService){
        this.productService = productService;
    }

    public ProductResponse getProductById(Long id){
        return ProductResponse.from(productService.getProductById(id));
    }

    public List<ProductResponse> getAllProducts(){
        return productService.getAllProducts().stream()
                .map(ProductResponse::from)
                .collect(Collectors.toList());
    }

}
