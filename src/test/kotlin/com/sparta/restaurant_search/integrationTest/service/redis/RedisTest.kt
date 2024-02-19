package com.sparta.restaurant_search.integrationTest.service.redis

import com.maxmind.geoip2.DatabaseReader
import com.sparta.restaurant_search.kakao.KakaoClient
import com.sparta.restaurant_search.naver.NaverClient
import com.sparta.restaurant_search.repository.FollowRepository
import com.sparta.restaurant_search.repository.PlaceRepository
import com.sparta.restaurant_search.repository.UserRepository
import com.sparta.restaurant_search.service.PlaceService
import io.mockk.mockk

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.redisson.api.RedissonClient
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.data.redis.core.ValueOperations
import org.springframework.test.context.ActiveProfiles
import org.testcontainers.junit.jupiter.Testcontainers
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import kotlin.math.log

@SpringBootTest
class RedisTest(): RedisContainer() {
    private var kakaoClient: KakaoClient = mockk()
    private var naverClient: NaverClient = mockk()
    private val placeRepository: PlaceRepository = mockk()
    private val userRepository: UserRepository = mockk()
    private val followRepository: FollowRepository = mockk()
    private val databaseReader: DatabaseReader = mockk()

    @Autowired
    lateinit var redissonClient: RedissonClient

    @Autowired
    lateinit var redisTemplate: StringRedisTemplate


    @Test
    fun 레디스_저장() {

        redisStore("test_search")
        var value = redisTemplate.opsForValue().get("test_search")
        Assertions.assertEquals("1", value)

        redisStore("test_search")
        value = redisTemplate.opsForValue().get("test_search")
        Assertions.assertEquals("2", value)

        redisStore("test_search")
        value = redisTemplate.opsForValue().get("test_search")
        Assertions.assertEquals("3", value)
    }

    @Test
    fun 레디스_TOP_5() {

        redisStore("test_search1")
        redisStore("test_search1")
        redisStore("test_search2")
        redisStore("test_search2")
        redisStore("test_search2")
        redisStore("test_search3")
        redisStore("test_search3")
        redisStore("test_search3")
        redisStore("test_search3")
        redisStore("test_search3")

        val placeService = PlaceService(kakaoClient, naverClient, redisTemplate, placeRepository, userRepository, followRepository, databaseReader, redissonClient)
        val result = placeService.findBestKeywords()

        for(keyword in result) {
            println(keyword)
        }
    }

    @Test
    fun 키워드_검색_동시성_테스트() {
        val search = "맛집"

        val numberOfThreads = 5
        val executor = Executors.newFixedThreadPool(numberOfThreads)
        val latch = CountDownLatch(5)

        executor.execute {
            redisStore(search)
            latch.countDown()
        }
        executor.execute {
            redisStore(search)
            latch.countDown()
        }
        executor.execute {
            redisStore(search)
            latch.countDown()
        }
        executor.execute {
            redisStore(search)
            latch.countDown()
        }
        executor.execute {
            redisStore(search)
            latch.countDown()
        }
        latch.await()

        val value = redisTemplate.opsForValue().get(search)

        Assertions.assertEquals("5", value)


    }

    private val lockPrefix = "lock:"

    private fun redisStore(request: String) {
        val lock = redissonClient.getLock(lockPrefix + request)
        try {
            if (lock.tryLock(500, 10, TimeUnit.MILLISECONDS)) {
                val operations: ValueOperations<String, String> = redisTemplate.opsForValue()

                val value = operations.get(request)

                if (value == null) {
                    operations.set(request, "1")
                } else {
                    // 키가 존재하면 값을 1씩 증가시키기
                    val incrementedValue = (value.toInt() + 1).toString()
                    operations.set(request, incrementedValue)
                }
            }
        } finally {
            lock.unlock()
        }
    }
}