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
    private val naverGeoSecret: String
) {
    private val hostName = "https://geolocation.apigw.ntruss.com"
    private val requestUrl = "/geolocation/v2/geoLocation"

    fun search(ip: String): SearchNaverGeoResponse {
        val sortedSet = TreeMap<String, String>()
        sortedSet["ip"] = ip
        sortedSet["responseFormatType"] = "json"

        val timeStamp = System.currentTimeMillis().toString()
        val queryString = sortedSet.map { "${it.key}=${it.value}" }.joinToString("&")
        val baseString = "$requestUrl?$queryString"
        val signature = makeSignature(naverGeoSecret, "GET", baseString, timeStamp, naverGeoId)

        val headers = HttpHeaders()
        headers.set("x-ncp-apigw-timestamp", timeStamp)
        headers.set("x-ncp-iam-access-key", naverGeoId)
        headers.set("x-ncp-apigw-signature-v2", signature)
        headers.contentType = MediaType.APPLICATION_JSON

        val restTemplate = RestTemplate()
        val response = restTemplate.getForObject("$hostName$baseString", String::class.java)
            ?: throw IllegalStateException("Failed to retrieve search results.")

        val jsonObject = ObjectMapper().readTree(response)
        val lat = jsonObject["lat"].asDouble()
        val long = jsonObject["long"].asDouble()
        return SearchNaverGeoResponse(lat, long)
    }

    private fun makeSignature(secretKey: String, method: String, baseString: String, timestamp: String, accessKey: String): String {
        val hmacSha256 = Mac.getInstance("HmacSHA256")
        val secretKeySpec = SecretKeySpec(secretKey.toByteArray(), "HmacSHA256")
        hmacSha256.init(secretKeySpec)

        val message = "$method\n$baseString\n$timestamp\n$accessKey"
        val hash = hmacSha256.doFinal(message.toByteArray())
        return Base64.getEncoder().encodeToString(hash)
    }
}