package com.example.ecommerce.user.integration;

import com.example.ecommerce.user.application.dto.UserCreateRequest;
import com.example.ecommerce.user.domain.model.User;
import com.example.ecommerce.user.domain.repository.UserRepository;
import com.example.ecommerce.user.domain.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//application.properties 또는 application.yml에서 지정한 실제 DB 설정을 그대로 사용
@Transactional //테스트가 끝난 직후 자동으로 트랜잭션이 롤백
public class UserIntegrationTest {
    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Test
    void 유저_생성_통합테스트() {
        // given
        UserCreateRequest request = new UserCreateRequest("seokhan");

        // when
        User user = userService.create(request);

        // then
        assertThat(user.getId()).isNotNull();
        assertThat(user.getName()).isEqualTo("seokhan");

        // DB에 저장되었는지도 확인
        User found = userRepository.findById(user.getId()).orElseThrow();
        assertThat(found.getName()).isEqualTo("seokhan");
    }
}
