package com.example.ecommerce.point.domain.model;

import com.example.ecommerce.user.domain.model.User;
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
    private Long amount; //변동금액
    private Long balanceAfter; // 변동 후 잔액

    @Enumerated(EnumType.STRING)
    private PointType type; // CHARGE or USE

    private LocalDateTime createdAt;

    public PointHistory(Long userId, Long amount, Long balanceAfter, PointType type) {
        this.userId = userId;
        this.amount = amount;
        this.balanceAfter = balanceAfter;
        this.type = type;
    }
}
