package com.sparta.restaurant_search.kakao

import com.sparta.restaurant_search.web.reponse.SearchKakaoResponse
import com.sparta.restaurant_search.web.request.SearchKakaoRequest
import com.sparta.restaurant_search.web.request.SearchKakaoWithLocationRequest
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.*
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder
import java.net.URI

@Component
class KakaoClient (
    @Value("\${kakao.api.key}")
    private val kakaoClientKey: String,

    @Value("\${kakao.url.search.local}")
    private val kakaoLocalSearchUrl: String
) {
    fun SearchKakaoRequest.buildUri(kakaoLocalSearchUrl: String): URI {
        return UriComponentsBuilder
            .fromUriString(kakaoLocalSearchUrl)
            .queryParams(this.toMultiValueMap())
            .build()
            .encode()
            .toUri()
    }

    fun createKakaoHeaders(kakaoClientKey: String): HttpHeaders {
        val headers = HttpHeaders()
        headers.set("Authorization", "KakaoAK $kakaoClientKey")
        headers.contentType = MediaType.APPLICATION_JSON
        return headers
    }

    fun performKakaoSearch(uri: URI, headers: HttpHeaders): ResponseEntity<SearchKakaoResponse> {
        val httpEntity = HttpEntity<Any>(headers)
        val responseType = object : ParameterizedTypeReference<SearchKakaoResponse>() {}
        return RestTemplate().exchange(uri, HttpMethod.GET, httpEntity, responseType)
    }

    fun localSearch(searchKakaoRequest: SearchKakaoRequest): SearchKakaoResponse {
        val uri = searchKakaoRequest.buildUri(kakaoLocalSearchUrl)
        val headers = createKakaoHeaders(kakaoClientKey)
        val responseEntity = performKakaoSearch(uri, headers)
        return responseEntity.body ?: throw IllegalStateException("Failed to retrieve search results.")
    }

    fun SearchKakaoWithLocationRequest.buildUriWithLocation(kakaoLocalSearchUrl: String): URI {
        return UriComponentsBuilder
            .fromUriString(kakaoLocalSearchUrl)
            .queryParams(this.toMultiValueMapWithLocation())
            .build()
            .encode()
            .toUri()
    }

    fun localSearchWithLocation(searchKakaoWithLocationRequest: SearchKakaoWithLocationRequest): SearchKakaoResponse {
        val uri = searchKakaoWithLocationRequest.buildUriWithLocation(kakaoLocalSearchUrl)
        val headers = createKakaoHeaders(kakaoClientKey)
        val responseEntity = performKakaoSearch(uri, headers)
        return responseEntity.body ?: throw IllegalStateException("Failed to retrieve search results.")
    }

}