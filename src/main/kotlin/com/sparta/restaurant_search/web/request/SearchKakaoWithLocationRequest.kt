package com.sparta.restaurant_search.web.request

import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap

data class SearchKakaoWithLocationRequest(
        private val query: String = "",
        private val x: Double? = 0.0,
        private val y: Double? = 0.0,
        private val size: Int = 15,
        private val sort: String = "accuracy"

) {
    fun toMultiValueMapWithLocation(): MultiValueMap<String, String> {
        val map = LinkedMultiValueMap<String, String>()
        map.add("query", query)
        map.add("x", x.toString())
        map.add("y", y.toString())
        map.add("size", size.toString())
        map.add("sort", sort)

        return map
    }
}
