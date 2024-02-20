package com.sparta.restaurant_search.naver

import com.sparta.restaurant_search.web.reponse.SearchNaverGeoResponse
import com.sparta.restaurant_search.web.reponse.SearchNaverResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder
import java.sql.Timestamp
import java.time.LocalDateTime

@Component
class NaverGeolocation(
    @Value("\${naver.geolocation.id}")
    private val naverGeoId: String,

    @Value("\${naver.geolocation.secret}")
    private val naverGeoSecret: String,

    @Value("\${naver.geolocation.url}")
    private val naverGeoUrl: String
) {
    fun search(ip: String): SearchNaverGeoResponse {
        val uri = UriComponentsBuilder
            .fromUriString(naverGeoUrl)
            .queryParam(ip)
            .build()
            .encode()
            .toUri()

        val headers = HttpHeaders()
        headers.set("x-ncp-apigw-timestamp", System.currentTimeMillis().toString())
        headers.set("x-ncp-iam-access-key", naverGeoId)
        headers.set("X-Naver-Client-Secret", naverGeoSecret)
        headers.contentType = MediaType.APPLICATION_JSON

        val httpEntity = HttpEntity<Any>(headers)
        val responseType = object : ParameterizedTypeReference<SearchNaverGeoResponse>() {}

        val responseEntity = RestTemplate().exchange(
            uri,
            HttpMethod.GET,
            httpEntity,
            responseType
        )

        return responseEntity.body ?: throw IllegalStateException("Failed to retrieve search results.")


    }
}