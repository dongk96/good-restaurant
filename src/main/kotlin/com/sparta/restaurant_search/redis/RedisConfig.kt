package com.sparta.restaurant_search.redis

import org.redisson.Redisson
import org.redisson.api.RedissonClient
import org.redisson.config.Config
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.connection.RedisPassword
import org.springframework.data.redis.connection.RedisStandaloneConfiguration
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.data.redis.serializer.StringRedisSerializer
import java.nio.charset.Charset

@Configuration
class RedisConfig {
    @Value("\${spring.data.redis.host}")
    private val host: String = ""
    @Value("\${spring.data.redis.port}")
    private val port1: Int = 0
    @Value("\${spring.data.redis.password}")
    private val password1: String = ""

    @Bean
    fun redissonClient(): RedissonClient {
        val config = Config()
        config.useSingleServer()
            .setAddress("redis://${host}:${port1}")
//            .setPassword(password1)
        return Redisson.create(config)
    }
}