package com.example.ecommerce.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

@Configuration
public class RedissonConfig {
    @Bean
    public RedissonClient redissonClient() {
        Config config = new Config();
        // 단일 노드 Redis
        config.useSingleServer()
                .setAddress("redis://127.0.0.1:6379")
                .setConnectionMinimumIdleSize(5)
                .setConnectionPoolSize(10);

        return Redisson.create(config);
    }
}
