package com.example.ecommerce.point.infrastructure;

import com.example.ecommerce.point.domain.model.PointHistory;
import com.example.ecommerce.point.domain.model.PointType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "point_history")
public class PointHistoryJpaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Long amount; // 변동 금액

    @Column(name = "balance_after", nullable = false)
    private Long balanceAfter; // 변동 후 잔액

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private PointType type;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    public PointHistory toDomain(){
        return PointHistory.builder()
                .id(id)
                .userId(userId)
                .amount(amount)
                .balanceAfter(balanceAfter)
                .type(type)
                .createdAt(createdAt)
                .build();
    }

    public static PointHistoryJpaEntity fromDomain(PointHistory pointHistory) {
        return PointHistoryJpaEntity.builder()
                .id(pointHistory.getId())
                .userId(pointHistory.getUserId())
                .amount(pointHistory.getAmount())
                .balanceAfter(pointHistory.getBalanceAfter())
                .type(pointHistory.getType())
                .createdAt(pointHistory.getCreatedAt())
                .build();
    }
}
