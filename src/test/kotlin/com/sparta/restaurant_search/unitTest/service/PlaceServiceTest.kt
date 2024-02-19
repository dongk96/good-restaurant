package com.sparta.restaurant_search.unitTest.service

import com.maxmind.geoip2.DatabaseReader
import com.sparta.restaurant_search.domain.dto.PlaceDto
import com.sparta.restaurant_search.exception.KakaoApiException
import com.sparta.restaurant_search.kakao.KakaoClient
import com.sparta.restaurant_search.naver.NaverClient
import com.sparta.restaurant_search.repository.FollowRepository
import com.sparta.restaurant_search.repository.PlaceRepository
import com.sparta.restaurant_search.repository.UserRepository
import com.sparta.restaurant_search.service.PlaceService
import com.sparta.restaurant_search.web.reponse.SearchNaverResponse
import com.sparta.restaurant_search.web.request.SearchKakaoRequest
import com.sparta.restaurant_search.web.request.SearchNaverRequest
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import junit.framework.TestCase.assertEquals
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.redisson.api.RedissonClient
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.data.redis.core.ValueOperations
import java.time.LocalDateTime

@SpringBootTest
class PlaceServiceTest {
    @Autowired
    private lateinit var kakaoClient: KakaoClient
    @Autowired
    private lateinit var naverClient: NaverClient
    private val placeRepository:PlaceRepository = mockk()
    private val userRepository:UserRepository = mockk()
    private val followRepository:FollowRepository = mockk()
    private val databaseReader: DatabaseReader = mockk()
    private val redissonClient:RedissonClient = mockk()

    @Test
    fun 카카오_기반_장소_찾기() {
        val search = SearchKakaoRequest(query = "당산역 맛집")
        val result = kakaoClient.localSearch(search)
        for(item in result.documents) {
            println(item)
        }
    }

    @Test
    fun 네이버_기반_장소_찾기() {
        val search = SearchNaverRequest(query = "당산역 맛집")
        val result = naverClient.localSearch(search)
        for(item in result.items) {
            println(item)
        }
    }

    @Test
    fun 맛집_찾기_카카오_오류() {
        val kakaoClient:KakaoClient = mockk()
        val naverClient:NaverClient = mockk()
        every { kakaoClient.localSearch(any()) } throws KakaoApiException("kakaoApi Error")
        val items: List<SearchNaverResponse.SearchLocalItem> =
            listOf(SearchNaverResponse.SearchLocalItem("맛집", "www.local.com", "010-1234-5678", "주소", "주소"),
            SearchNaverResponse.SearchLocalItem("맛집", "www.local.com", "010-1234-5678", "주소", "주소"))

        every { naverClient.localSearch(any()) } returns SearchNaverResponse("2024-02-13", 2, 1, 1, items)

        val redisTemplate = mockk<StringRedisTemplate>()
        val valueOperations = mockk<ValueOperations<String, String>>()

        every { redisTemplate.opsForValue() } returns valueOperations

        every { valueOperations.get(any()) } returns null
        every { valueOperations.set(any(), any()) } just runs

        val placeService = PlaceService(kakaoClient, naverClient, redisTemplate, placeRepository, userRepository, followRepository, databaseReader, redissonClient)

        val result = placeService.findPlaces("some request")

        val expected = listOf(
            PlaceDto(null, "맛집", "주소", "www.local.com"),
            PlaceDto(null, "맛집", "주소", "www.local.com")
        )
        Assertions.assertEquals(expected, result)
    }

    @Test
    fun 현재_IP_가져오기() {
        TODO()
    }
}