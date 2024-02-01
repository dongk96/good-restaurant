package com.sparta.restaurant_search.kakao

import com.sparta.restaurant_search.web.request.SearchKakaoRequest
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class SearchDeliciousPlaceTest {

    @Autowired
    private lateinit var kakaoClient: KakaoClient

    @Test
    fun searchLocalTest() {
        val search = SearchKakaoRequest(query = "강남역 횟집")
        val result = kakaoClient.localSearch(search)
        for (item in result.documents) {
            println(item)
        }

    }
}