package com.example.ecommerce.docs.controller;

import com.example.ecommerce.docs.dto.CommApiResponse;
import com.example.ecommerce.user.application.dto.UserCreateRequest;
import com.example.ecommerce.user.application.dto.UserCreateResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "User", description = "User API")
public interface UserApi {
    @Operation(summary = "유저 생성", description = "유저를 생성하는 API입니다.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "유저 생성 성공",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "성공 응답",
                                    value = """
                                        {
                                          "code": 200,
                                          "message": "요청이 정상적으로 처리되었습니다.",
                                          "data": {
                                            "id": 1,
                                            "name": "홍길동"
                                          }
                                        }
                                        """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "에러 응답 예시",
                                    value = """
                                        {
                                          "code": 400,
                                          "message": "잘못된 요청입니다.",
                                          "data": null
                                        }
                                        """
                            )
                    )
            )
    })
    @PostMapping("/api/v1/users")
    CommApiResponse<UserCreateResponse> createUser(@RequestBody UserCreateRequest request);
}
