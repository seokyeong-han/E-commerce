package com.example.ecommerce.point.domain.model;

import com.example.ecommerce.user.domain.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class Point {
    private Long id;
    private Long userId;
    private Long balance;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Point(User user) {
        this.userId = user.getId();
        this.balance = 0L;
    }

    public void earn(Long amount) {
        if (amount <= 0) throw new IllegalArgumentException("충전 금액은 0보다 커야 합니다.");
        this.balance += amount;
    }

    public void use(int amount) {
        if (amount <= 0) throw new IllegalArgumentException("사용 금액은 0보다 커야 합니다.");
        if (balance < amount) throw new IllegalStateException("잔액 부족");
        this.balance -= amount;
    }
}
