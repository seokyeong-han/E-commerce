package com.example.ecommerce.product.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class Product {
    private Long id;
    private String name;
    private Long price;
    private Integer stock;
    private LocalDateTime createdAt;

    //재고 차감
    public void decreaseStock(int quantity) {
        if (this.stock < quantity) {
            throw new IllegalArgumentException("재고 부족");
        }
        this.stock -= quantity;
    }
}
