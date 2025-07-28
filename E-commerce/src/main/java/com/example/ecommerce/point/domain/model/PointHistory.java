package com.example.ecommerce.point.domain.model;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class PointHistory {
    private Long id;
    private Long userId;
    private Long amount;

    @Enumerated(EnumType.STRING)
    private PointType type; // CHARGE or USE

    private LocalDateTime createdAt;
}
