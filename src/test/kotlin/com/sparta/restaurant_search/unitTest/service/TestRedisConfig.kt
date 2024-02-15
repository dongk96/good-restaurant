package com.sparta.restaurant_search.unitTest.service

import com.sparta.restaurant_search.unitTest.service.RedisContainer.Companion.redisContainer
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory


@TestConfiguration
class TestRedisConfig {

    companion object {
        @Bean
        fun testRedisConnectionFactory(): RedisConnectionFactory {
            val host = redisContainer.host
            val port = redisContainer.getMappedPort(6379)
            return LettuceConnectionFactory(host, port)
        }
    }
}