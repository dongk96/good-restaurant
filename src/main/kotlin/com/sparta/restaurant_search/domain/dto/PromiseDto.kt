package com.sparta.restaurant_search.domain.dto

import com.sparta.restaurant_search.domain.entity.Promise

data class PromiseDto(
    var promiseId: Long,
    var partnerId: Long,
    var placeName: String,
    var placeAddress: String,
    var placeUrl: String,
    var statement: Boolean
) {
    companion object {
        fun fromReceiveEntities(promises: List<Promise>): List<PromiseDto> {
            return promises.map {
                val dto = PromiseDto(
                    it.promiseId!!,
                    it.requester.userId!!,
                    it.place.placeName,
                    it.place.placeAddress,
                    it.place.placeUrl,
                    it.statement
                )

                dto
            }
        }

        fun fromRequestEntities(promises: List<Promise>): List<PromiseDto> {
            return promises.map {
                val dto = PromiseDto(
                    it.promiseId!!,
                    it.respondent.userId!!,
                    it.place.placeName,
                    it.place.placeAddress,
                    it.place.placeUrl,
                    it.statement
                )

                dto
            }
        }
    }
}