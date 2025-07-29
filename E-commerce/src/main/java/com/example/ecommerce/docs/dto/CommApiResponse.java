package com.example.ecommerce.docs.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "공통 응답 포맷")
public class CommApiResponse<T> {

    @Schema(description = "응답 코드", example = "200")
    private int code;

    @Schema(description = "응답 메시지", example = "요청이 정상적으로 처리되었습니다.")
    private String message;

    @Schema(description = "응답 데이터")
    private T data;

    public static <T> CommApiResponse<T> success(T data) {
        return CommApiResponse.<T>builder()
                .code(200)
                .message("요청이 정상적으로 처리되었습니다.")
                .data(data)
                .build();
    }

    public static <T> CommApiResponse<T> error(int code, String message) {
        return CommApiResponse.<T>builder()
                .code(code)
                .message(message)
                .data(null)
                .build();
    }
}
