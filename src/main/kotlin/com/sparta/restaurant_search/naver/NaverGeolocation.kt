package com.sparta.restaurant_search.naver

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

) {
    val accessKey: String = System.getenv(naverGeoId) // 발급받은 API Key 입력
    val secretKey: String = System.getenv(naverGeoSecret) // 발급받은 Secret Key 입력
    val hostName = "https://geolocation.apigw.ntruss.com"
    val requestUrl = "/geolocation/v2/geoLocation"

    fun search(ip: String): SearchNaverGeoResponse {

        val sortedSet = TreeMap<String, String>()
        sortedSet["ip"] = ip
        sortedSet["ext"] = "t"
        sortedSet["responseFormatType"] = "json"

        val timeStamp = System.currentTimeMillis().toString()
        val queryString = sortedSet.map { "${it.key}=${it.value}" }.joinToString("&")
        val baseString = "$requestUrl?$queryString"
        val signature = makeSignature(secretKey, "GET", baseString, timeStamp, accessKey)


        val headers = HttpHeaders()
        headers.set("x-ncp-apigw-timestamp", System.currentTimeMillis().toString())
        headers.set("x-ncp-iam-access-key", naverGeoId)
        headers.set("X-Naver-Client-Secret", naverGeoSecret)
        headers.contentType = MediaType.APPLICATION_JSON

        val restTemplate = RestTemplate()
        val response = restTemplate.getForObject("$hostName$baseString", String::class.java)
        if (response != null) {
            val jsonObject = JSONObject(response)
            val lat = jsonObject.getDouble("lat")
            val long = jsonObject.getDouble("long")
            return SearchNaverGeoResponse(lat, long)
        } else {
            throw IllegalStateException("Failed to retrieve search results.")
        }
    }

    fun makeSignature(secretKey: String, method: String, baseString: String, timestamp: String, accessKey: String): String {
        val hmacSha256 = Mac.getInstance("HmacSHA256")
        val secretKeySpec = SecretKeySpec(secretKey.toByteArray(), "HmacSHA256")
        hmacSha256.init(secretKeySpec)

        val message = "$method\n$baseString\n$timestamp\n$accessKey"
        val hash = hmacSha256.doFinal(message.toByteArray())
        return Base64.getEncoder().encodeToString(hash)
    }
}