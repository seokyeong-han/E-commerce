package com.example.ecommerce.point.application.dto;

import com.example.ecommerce.point.domain.model.Point;
import com.example.ecommerce.point.domain.model.PointCommand;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class PointChargeResponse {
    private Long userId;
    private Long balance;       //충전 후 금액
    private Long chargedAmount; //충전금액

    public PointChargeResponse(Long userId, Long balance, Long chargedAmount) {
        this.userId = userId;
        this.balance = balance;
        this.chargedAmount = chargedAmount;
    }

    public static PointChargeResponse of(Point point, PointCommand.Charge command) {
        return new PointChargeResponse(
                point.getUserId(),
                point.getBalance(),
                command.getAmount()
        );
    }
}
