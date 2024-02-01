package com.sparta.restaurant_search.naver

import com.sparta.restaurant_search.web.reponse.SearchKakaoResponse
import com.sparta.restaurant_search.web.reponse.SearchNaverResponse
import com.sparta.restaurant_search.web.request.SearchKakaoRequest
import com.sparta.restaurant_search.web.request.SearchNaverRequest
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder

@Component
class NaverClient (
    @Value("\${naver.client.id}")
    private val naverClientId: String,

    @Value("\${naver.client.secret}")
    private val naverSecret: String,

    @Value("\${naver.url.search.local}")
    private val naverLocalSearchUrl: String
) {
    fun localSearch(searchNaverRequest: SearchNaverRequest): SearchNaverResponse {
        val uri = UriComponentsBuilder
            .fromUriString(naverLocalSearchUrl)
            .queryParams(searchNaverRequest.toMultiValueMap())
            .build()
            .encode()
            .toUri()

        val headers = HttpHeaders()
        headers.set("X-Naver-Client-Id", naverClientId)
        headers.set("X-Naver-Client-Secret", naverSecret)
        headers.contentType = MediaType.APPLICATION_JSON

        val httpEntity = HttpEntity<Any>(headers)
        val responseType = object : ParameterizedTypeReference<SearchNaverResponse>() {}

        val responseEntity = RestTemplate().exchange(
            uri,
            HttpMethod.GET,
            httpEntity,
            responseType
        )

        return responseEntity.body ?: throw IllegalStateException("Failed to retrieve search results.")
    }
}