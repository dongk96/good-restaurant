package com.sparta.restaurant_search.web.request

import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap

data class SearchKakaoRequest(
    private val query: String = "",
    private val size: Int = 15,
    private val sort: String = "accuracy"

) {
    fun toMultiValueMap(): MultiValueMap<String, String> {
        val map = LinkedMultiValueMap<String, String>()
        map.add("query", query)
        map.add("size", size.toString())
        map.add("sort", sort)

        return map
    }
}