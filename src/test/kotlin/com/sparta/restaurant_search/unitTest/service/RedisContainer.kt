package com.sparta.restaurant_search.unitTest.service

import org.junit.jupiter.api.BeforeAll
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.test.context.ActiveProfiles
import org.testcontainers.containers.GenericContainer
import org.testcontainers.junit.jupiter.Container


@ActiveProfiles("test")
abstract class RedisContainer {

    companion object {
        @Container
        val redisContainer: GenericContainer<Nothing> = GenericContainer<Nothing>("redis:latest").apply {
            withExposedPorts(6379)
            start()
        }
    }
}