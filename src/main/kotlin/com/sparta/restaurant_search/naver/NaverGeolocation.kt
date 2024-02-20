package com.sparta.restaurant_search.naver

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.gson.JsonParser
import com.sparta.restaurant_search.web.reponse.SearchNaverGeoResponse
import com.sparta.restaurant_search.web.reponse.SearchNaverResponse
import org.apache.commons.codec.binary.Base64
import org.apache.http.client.config.RequestConfig
import org.apache.http.client.methods.CloseableHttpResponse
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.CloseableHttpClient
import org.apache.http.impl.client.HttpClientBuilder
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
import java.io.BufferedReader
import java.io.InputStreamReader
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
    private val httpClient: CloseableHttpClient

    init {
        val timeout = 5000
        val requestConfig = RequestConfig.custom().setSocketTimeout(timeout).setConnectTimeout(timeout).build()
        httpClient = HttpClientBuilder.create().setDefaultRequestConfig(requestConfig).build()
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            if (args.size != 3) {
                println("Usage : accessKey secretKey ip")
                return
            }

            try {
                val apiClient = NaverGeolocation(args[0], args[1])
                val response = apiClient.searchNaverGeo(args[2])
                println("Latitude: ${response.lat}, Longitude: ${response.long}")
            } catch (e: Exception) {
                println(e.message)
            }
        }

        private fun convertTypeToSortedMap(requestParameters: Map<String, List<String>>): SortedMap<String, SortedSet<String>> {
            val significateParameters = TreeMap<String, SortedSet<String>>()
            for ((parameterName, parameterValues) in requestParameters) {
                val significantValues = TreeSet<String>()
                parameterValues?.let {
                    for (parameterValue in it) {
                        significantValues.add(parameterValue ?: "")
                    }
                }
                significateParameters[parameterName] = significantValues
            }
            return significateParameters
        }
    }

    fun searchNaverGeo(ip: String): SearchNaverGeoResponse {
        val requestMethod = "GET"
        val hostName = "https://geolocation.apigw.fin-ntruss.com"
        val requestUrl = "/geolocation/v2/geoLocation"

        val requestParameters = mapOf(
            "ip" to listOf(ip),
            "ext" to listOf("t"),
            "responseFormatType" to listOf("json")
        )
        val parameters = convertTypeToSortedMap(requestParameters)

        val timestamp = generateTimestamp()
        println("timestamp: $timestamp")

        val baseString = "$requestUrl?${getRequestQueryString(parameters)}"
        println("baseString : $baseString")

        val signature = makeSignature(requestMethod, baseString, timestamp, naverGeoId, naverGeoSecret)
        println("signature : $signature")

        val requestFullUrl = "$hostName$baseString"
        val request = HttpGet(requestFullUrl)
        request.setHeader("x-ncp-apigw-timestamp", timestamp)
        request.setHeader("x-ncp-iam-access-key", naverGeoId)
        request.setHeader("x-ncp-apigw-signature-v2", signature)
        val response: CloseableHttpResponse = httpClient.execute(request)

        val msg = getResponse(response)
        println(msg)

        // Parse response to SearchNaverGeoResponse
        val (lat, long) = parseResponseToLatLng(msg)
        return SearchNaverGeoResponse(lat, long)
    }

    private fun getResponse(response: CloseableHttpResponse): String {
        val buffer = StringBuffer()
        val reader = BufferedReader(InputStreamReader(response.entity.content))
        var msg: String?

        try {
            while (reader.readLine().also { msg = it } != null) {
                buffer.append(msg)
            }
        } catch (e: Exception) {
            throw e
        } finally {
            response.close()
        }
        return buffer.toString()
    }

    private fun parseResponseToLatLng(response: String): Pair<Double, Double> {
        val jsonObject = JsonParser.parseString(response).asJsonObject

        val lat = jsonObject.getAsJsonObject("location").getAsJsonPrimitive("lat").asDouble
        val long = jsonObject.getAsJsonObject("location").getAsJsonPrimitive("long").asDouble

        return Pair(lat, long)
    }

    private fun generateTimestamp(): String {
        return System.currentTimeMillis().toString()
    }

    private fun getRequestQueryString(significantParameters: SortedMap<String, SortedSet<String>>): String {
        val queryString = StringBuilder()
        val paramIt = significantParameters.entries.iterator()
        while (paramIt.hasNext()) {
            val sortedParameter = paramIt.next()
            val valueIt = sortedParameter.value.iterator()
            while (valueIt.hasNext()) {
                val parameterValue = valueIt.next()
                queryString.append("${sortedParameter.key}=$parameterValue")
                if (paramIt.hasNext() || valueIt.hasNext()) {
                    queryString.append('&')
                }
            }
        }
        return queryString.toString()
    }

    private fun makeSignature(
        method: String,
        baseString: String,
        timestamp: String,
        accessKey: String,
        secretKey: String
    ): String {
        val space = " "
        val newLine = "\n"

        val message = StringBuilder()
            .append(method)
            .append(space)
            .append(baseString)
            .append(newLine)
            .append(timestamp)
            .append(newLine)
            .append(accessKey)
            .toString()

        val signingKey = SecretKeySpec(secretKey.toByteArray(charset("UTF-8")), "HmacSHA256")
        val mac = Mac.getInstance("HmacSHA256")
        mac.init(signingKey)
        val rawHmac = mac.doFinal(message.toByteArray(charset("UTF-8")))
        return Base64.encodeBase64String(rawHmac)
    }
}