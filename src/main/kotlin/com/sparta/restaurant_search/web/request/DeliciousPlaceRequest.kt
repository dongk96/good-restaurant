package com.sparta.restaurant_search.web.request

data class DeliciousPlaceRequest (
    var placeId: Long,
    var placeName: String,
    var placeAddress: String,
    var placeUrl: String
)