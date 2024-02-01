package com.sparta.restaurant_search.domain.dto

data class BestPlaceDto (
    val placeId: Long,
    val placeName: String,
    val placeAddress: String,
    val placeUrl: String,
    val placeCount: Long
)