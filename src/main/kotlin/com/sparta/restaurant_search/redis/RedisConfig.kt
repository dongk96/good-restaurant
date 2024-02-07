package com.sparta.restaurant_search.redis

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.connection.RedisPassword
import org.springframework.data.redis.connection.RedisStandaloneConfiguration
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.serializer.StringRedisSerializer
import java.nio.charset.Charset

@Configuration
class RedisConfig {
    @Value("\${spring.data.redis.host}")
    private val host: String = ""
    @Value("\${spring.data.redis.port}")
    private val port: Int = 0
    @Value("\${spring.data.redis.password}")
    private val password: String = ""

    @Bean
    fun redisConnectionFactory(): RedisConnectionFactory {
        val redisStandaloneConfiguration = RedisStandaloneConfiguration().apply {
            hostName = host
            port = port
            password = password
        }

        val lettuceConnectionFactory = LettuceConnectionFactory(redisStandaloneConfiguration)
        return lettuceConnectionFactory
    }

    @Bean
    fun redisTemplate(redisConnectionFactory: RedisConnectionFactory): RedisTemplate<String, String> {
        val redisTemplate = RedisTemplate<String, String>()
        redisTemplate.connectionFactory = redisConnectionFactory()
        val serializer = StringRedisSerializer(Charset.forName("UTF-8"))
        redisTemplate.keySerializer = serializer
        redisTemplate.valueSerializer = serializer
//        redisTemplate.hashKeySerializer = serializer
//        redisTemplate.hashValueSerializer = serializer
        return redisTemplate
    }
}