package com.example.ecommerce.point.application.controller;

import com.example.ecommerce.point.application.dto.UserPointResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("api/points")
public class PointController {

    @GetMapping("/{userId}")
    public ResponseEntity<UserPointResponse> getPoint(@PathVariable Long userId){
        UserPointResponse res = null;
        return ResponseEntity.ok(res);
    }
}
