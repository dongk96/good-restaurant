package com.sparta.restaurant_search.integrationTest.service.redis

import com.github.dockerjava.api.model.PortBinding
import org.junit.jupiter.api.BeforeAll
import org.springframework.beans.factory.annotation.Value
import org.springframework.test.context.ActiveProfiles
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.wait.strategy.Wait
import org.testcontainers.junit.jupiter.Container

@ActiveProfiles("test")
abstract class RedisContainer {

    companion object {
        private const val REDIS_PORT = 6379

        private val redisContainer: GenericContainer<Nothing> = GenericContainer<Nothing>("redis:latest").apply {
            withExposedPorts(REDIS_PORT)
            start()
        }

        init {
            System.setProperty("spring.data.redis.port", redisContainer.host)
            System.setProperty("spring.data.redis.port", redisContainer.getMappedPort(6379).toString())
        }
    }

}