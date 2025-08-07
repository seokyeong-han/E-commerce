package com.example.ecommerce.point.application.dto;

import com.example.ecommerce.point.domain.model.Point;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class UserPointResponse {
    private Long userId;
    private Long balance;

    public UserPointResponse(Long userId, Long balance) {
        this.userId = userId;
        this.balance = balance;
    }

    public static UserPointResponse from(Point point) {
        return new  UserPointResponse(point.getUserId(), point.getBalance());
    }
}
