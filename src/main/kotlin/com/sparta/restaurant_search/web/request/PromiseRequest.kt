package com.sparta.restaurant_search.web.request

data class PromiseRequest(
    var placeId: String?,
    var placeName: String,
    var respondentId: Long
)
