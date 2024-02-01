package com.sparta.restaurant_search.web.request

import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap

data class SearchNaverRequest (
    var query: String = "",
    var display: Int = 5,
    var start: Int = 1,
    var sort: String = "random"
) {
    fun toMultiValueMap(): MultiValueMap<String, String> {
        val map = LinkedMultiValueMap<String, String>()
        map.add("query", query)
        map.add("display", display.toString())
        map.add("start", start.toString())
        map.add("sort", sort)
        return map
    }
}