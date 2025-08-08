package com.example.ecommerce.product.application.controller;

import com.example.ecommerce.docs.dto.CommApiResponse;
import com.example.ecommerce.product.application.dto.ProductResponse;
import com.example.ecommerce.product.application.facade.ProductFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("api/products")
public class ProductController {

    private final ProductFacade productFacade;

    public ProductController(ProductFacade productFacade) {
        this.productFacade = productFacade;
    }

    @Operation(summary = "전체 상품 조회", description = "모든 상품 정보를 조회합니다.")
    @ApiResponse(
            responseCode = "200",
            description = "조회 성공",
            content = @Content(schema = @Schema(implementation = ProductResponse.class))
    )
    @GetMapping
    public ResponseEntity<CommApiResponse<List<ProductResponse>>> getAllProducts() {
        List<ProductResponse> response = productFacade.getAllProducts();
        return ResponseEntity.ok(CommApiResponse.success(response));
    }

    @Operation(summary = "상품 단건 조회", description = "상품 ID로 단일 상품을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(
                    schema = @Schema(implementation = ProductResponse.class)
            )),
            @ApiResponse(responseCode = "404", description = "상품을 찾을 수 없음")
    })
    @GetMapping("/{id}")
    public ResponseEntity<CommApiResponse<ProductResponse>> getProduct(@PathVariable Long id) {
        ProductResponse response = productFacade.getProductById(id);
        return ResponseEntity.ok(CommApiResponse.success(response));
    }


}
