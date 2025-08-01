package com.example.ecommerce.docs.controller.point;

import com.example.ecommerce.docs.dto.CommApiResponse;
import com.example.ecommerce.point.application.dto.UserPointResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Tag(name = "Point", description = "Point API")
public interface PointApi {

    @Operation(summary = "유저 포인트 검색", description = "유저 포인트 검색 API")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "성공 응답",
                                    value = """
                {
                  "code": 200,
                  "message": "요청이 정상적으로 처리되었습니다.",
                  "data": {
                    "userId": 1,
                    "balance": 1000
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
                                    name = "에러 응답",
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
    @GetMapping("/points/{userId}")
    CommApiResponse<UserPointResponse> getPoint(@PathVariable Long userId);
}
