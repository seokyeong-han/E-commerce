package com.example.ecommerce.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public class CommApiResponse<T> {
    @Schema(description = "응답 코드", example = "200")
    private int code;

    @Schema(description = "응답 메시지", example = "요청이 정상 처리되었습니다.")
    private String message;

    @Schema(description = "실제 응답 데이터")
    private T data;
}
