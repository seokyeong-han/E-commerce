package com.example.ecommerce.product.application.dto;

import com.example.ecommerce.product.domain.model.Product;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class ProductResponse {
    private Long id;
    private String name;
    private Long price;
    private Integer stock;

    public ProductResponse(Long id, String name, Long price, Integer stock) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
    }

    public static   ProductResponse from(Product product){
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getStock()
        );
    }
}
