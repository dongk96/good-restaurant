package com.sparta.restaurant_search.domain.dto

import com.sparta.restaurant_search.domain.entity.DeliciousPlace
import com.sparta.restaurant_search.web.reponse.SearchKakaoResponse
import com.sparta.restaurant_search.web.reponse.SearchNaverResponse

data class PlaceDto(
    var placeId: Long?,
    var placeName: String,
    var placeAddress: String,
    var placeUrl: String
) {
    companion object {
        fun fromKakao(places: SearchKakaoResponse): List<PlaceDto> {
            return places.documents.map {
                PlaceDto(
                    it.id,
                    it.place_name,
                    it.road_address_name,
                    it.place_url
                )
            }.toList()
        }

        fun fromNaver(places: SearchNaverResponse): List<PlaceDto> {
            return places.items.map {
                PlaceDto(
                    null,
                    it.title,
                    it.roadAddress,
                    it.link

                )
            }.toList()
        }

        fun fromEntities(deliciousPlaces: List<DeliciousPlace>): List<PlaceDto> {
            return deliciousPlaces.map {
                val dto = PlaceDto(
                    it.placeId!!,
                    it.placeName,
                    it.placeAddress,
                    it.placeUrl
                )

                dto
            }
        }
    }
}
