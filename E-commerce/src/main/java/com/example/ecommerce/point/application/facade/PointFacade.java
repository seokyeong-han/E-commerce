package com.example.ecommerce.point.application.facade;

import com.example.ecommerce.point.application.dto.PointChargeRequest;
import com.example.ecommerce.point.application.dto.PointChargeResponse;
import com.example.ecommerce.point.application.dto.UserPointResponse;
import com.example.ecommerce.point.domain.model.Point;
import com.example.ecommerce.point.domain.model.PointCommand;
import com.example.ecommerce.point.domain.model.PointHistory;
import com.example.ecommerce.point.domain.service.PointHistoryService;
import com.example.ecommerce.point.domain.service.PointService;
import com.example.ecommerce.user.domain.service.UserService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class PointFacade {
    private final PointService pointService;
    private final PointHistoryService pointHistoryService;
    private final UserService userService;

    public PointFacade(PointService pointService,  UserService userService,   PointHistoryService pointHistoryService) {
        this.pointService = pointService;
        this.pointHistoryService = pointHistoryService;
        this.userService = userService;
    }

    public UserPointResponse getUserPoints(Long userId){
        //유저검증
        userService.findById(userId);
        //포인트 조회
        Point point = pointService.getUserPints(userId);
        return UserPointResponse.from(point);
    }

    @Transactional
    public PointChargeResponse charge(PointChargeRequest req){
        // ✔️ Facade에서 DTO → Command 변환 후 Service에 전달하는 게 클린 아키텍처 원칙에 부합
        // ❌ Web용 DTO(PointChargeRequest)를 Service에 그대로 넘기는 건 계층 침범임
        // DTO → Command 변환, 도메인/서비스 계층에서 사용할 입력 모델 생성
        PointCommand.Charge command = new PointCommand.Charge(req.getUserId(), req.getAmount());
        //충전
        Point point = pointService.charge(command);
        //히스토리 저장
        pointHistoryService.saveChargeHistory(req.getUserId(), req.getAmount(), point.getBalance());

        return PointChargeResponse.of(point, command);
    }

}
