package com.example.ecommerce.user.application.controller;

import com.example.ecommerce.user.application.dto.UserCreateRequest;
import com.example.ecommerce.user.application.dto.UserCreateResponse;
import com.example.ecommerce.user.application.facade.UserFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("api/users")
public class UserController {

    @Autowired
    UserFacade  userFacade;

    @PostMapping("/create")
    public ResponseEntity<UserCreateResponse> creat(@RequestBody UserCreateRequest request){
        UserCreateResponse response = userFacade.createUserWithInitialPoint(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

}
