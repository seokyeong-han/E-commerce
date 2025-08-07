package com.example.ecommerce.point.application.controller;

import com.example.ecommerce.point.application.dto.PointChargeRequest;
import com.example.ecommerce.point.application.dto.PointChargeResponse;
import com.example.ecommerce.point.application.dto.UserPointResponse;
import com.example.ecommerce.point.application.facade.PointFacade;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("api/points")
public class PointController {
    private PointFacade pointFacade;

    public PointController(PointFacade pointFacade) {
        this.pointFacade = pointFacade;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserPointResponse> getUserPoints(@PathVariable Long userId){
        UserPointResponse res = pointFacade.getUserPoints(userId);
        return ResponseEntity.ok(res);
    }

    @PostMapping("/charge")
    public ResponseEntity<PointChargeResponse> charge(@RequestBody PointChargeRequest req){
        PointChargeResponse res = pointFacade.charge(req);
        return ResponseEntity.ok(res);
    }

}
