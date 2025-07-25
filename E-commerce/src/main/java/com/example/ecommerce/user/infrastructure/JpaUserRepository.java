package com.example.ecommerce.user.infrastructure;


import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaUserRepository extends JpaRepository<UserJpaEntity, Long> {
}
