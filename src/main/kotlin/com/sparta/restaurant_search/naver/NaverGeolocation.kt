package com.sparta.restaurant_search.naver

import com.fasterxml.jackson.databind.ObjectMapper
import com.sparta.restaurant_search.web.reponse.SearchNaverGeoResponse
import com.sparta.restaurant_search.web.reponse.SearchNaverResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.configurationprocessor.json.JSONObject
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
import java.util.*
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

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

        val timestamp = System.currentTimeMillis().toString()

        val uri = UriComponentsBuilder
            .fromUriString(naverGeoUrl)
            .queryParam(ip)
            .build()
            .encode()
            .toUri()

        val signature = makeSignature("GET", timestamp, naverGeoId, naverGeoSecret)

        val headers = HttpHeaders()
        headers.set("x-ncp-apigw-timestamp", timestamp)
        headers.set("x-ncp-iam-access-key", naverGeoId)
        headers.set("x-ncp-apigw-signature-v2", naverGeoSecret)
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

    fun makeSignature(
        method: String,
        timestamp: String,
        accessKey: String,
        secretKey: String
    ): String {
        val hmacSha256 = Mac.getInstance("HmacSHA256")
        val secretKeySpec = SecretKeySpec(secretKey.toByteArray(), "HmacSHA256")
        hmacSha256.init(secretKeySpec)

        val message = "$method\n$timestamp\n$accessKey"
        val hash = hmacSha256.doFinal(message.toByteArray())
        return Base64.getEncoder().encodeToString(hash)
    }
}