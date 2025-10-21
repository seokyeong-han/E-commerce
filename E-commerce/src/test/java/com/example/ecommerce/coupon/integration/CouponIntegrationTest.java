package com.example.ecommerce.coupon.integration;

import com.example.ecommerce.coupon.application.dto.CreateCouponRequest;
import com.example.ecommerce.coupon.domain.model.DiscountType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
// 1. 이 클래스에서 post, status 등을 static import 합니다.
import java.time.LocalDateTime;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Testcontainers
@SpringBootTest //Spring Application Context 로드
@AutoConfigureMockMvc //웹 계층(Controller)을 테스트하기 위한 MockMvc 객체를 자동으로 설정하고 Bean으로 등록
@ActiveProfiles("test")
public class CouponIntegrationTest {
    //redis 검증 테스트는 couponService기능이 아니라 redis기능은 CouponEventListener 책임 이므로 couponService 테스트에 포함 x

    @Container
    @ServiceConnection // DataSource 자동 구성
    static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0");

    @Container
    @ServiceConnection // Redis 자동 구성 모든 (GenericContainer :Docker 이미지를 다 띄울 수 있는 범용 클래스)
    static GenericContainer<?> redis = new GenericContainer<>("redis:7.2.4-alpine")
            .withExposedPorts(6379);

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private RedissonClient redissonClient;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void 쿠폰_발급_API를_호출하면_DB와_Redis에_정상적으로_저장된다() throws Exception {
        // given (준비)
        CreateCouponRequest request = CreateCouponRequest.builder()
                .name("웰컴쿠폰")
                .type(DiscountType.FIXED)
                .discountAmount(1000L)
                .totalQuantity(100)
                .activeFrom(LocalDateTime.of(2025, 12, 30, 23, 0, 0))
                .expiredAt(LocalDateTime.of(2025, 12, 31, 23, 59, 59))
                .build();
        // 3. ObjectMapper를 사용해 DTO 객체를 JSON 문자열로 변환합니다.
        // LocalDateTime 직렬화를 위해 JavaTimeModule 등록
        objectMapper.registerModule(new JavaTimeModule());
        String requestBody = objectMapper.writeValueAsString(request);

        // when (실행)
        MvcResult result = mockMvc.perform(post("/api/coupons") // 실제 API 경로로 수정하세요
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                )
                .andExpect(status().isOk()) // 또는 isCreated() (201)
                .andExpect(jsonPath("$.data.id").exists()) // 응답에 couponId가 있는지 확인
                .andReturn(); // 결과를 받아오기 위해 andReturn() 호출

        // then (검증)
        // 1. API 응답에서 생성된 쿠폰 ID 추출하기
        String responseBody = result.getResponse().getContentAsString();
        Map<String, Object> responseMap = objectMapper.readValue(responseBody, Map.class);
        Map<String, Object> dataMap = (Map<String, Object>) responseMap.get("data");
        Integer couponId = (Integer) dataMap.get("id");

        // 2. 추출한 ID를 이용해 Redis에서 데이터 조회하기
        String redisKey = "coupon:meta:" + couponId;
        RMap<String, String> couponMeta = redissonClient.getMap(redisKey);

        // 3. Redis에 저장된 데이터가 요청한 데이터와 일치하는지 검증하기
        assertTrue(couponMeta.isExists(), "Redis에 쿠폰 메타 데이터가 존재해야 합니다.");
        assertEquals(String.valueOf(request.getTotalQuantity()), couponMeta.get("limit"));

    }



}
