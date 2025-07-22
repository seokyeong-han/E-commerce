package com.example.ecommerce.user.domain.service;

import com.example.ecommerce.user.domain.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.example.ecommerce.user.application.dto.UserCreateRequest;
import com.example.ecommerce.user.domain.model.User;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void 유저_생성() {
        // given
        UserCreateRequest request = new UserCreateRequest("seokhan");
        User savedUser = request.toEntity();

        // save 동작 mocking (ID는 DB에서 생성된다고 가정)
        ReflectionTestUtils.setField(savedUser, "id", 1L);
        given(userRepository.save(any(User.class))).willReturn(savedUser);

        // when
        User result = userService.create(request);

        System.out.println("여어어어기 ----> "+result.getId());
        System.out.println("여어어어기 ----> "+result.getName());

        // then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("seokhan");


    }
}