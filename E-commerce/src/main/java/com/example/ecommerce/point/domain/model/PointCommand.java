package com.example.ecommerce.point.domain.model;

public class PointCommand {
    public static class Charge {
        private final Long userId;
        private final Long amount;

        public Charge(Long userId, Long amount) {
            this.userId = userId;
            this.amount = amount;
        }

        public Long getUserId() { return userId; }
        public Long getAmount() { return amount; }
    }
}
