package com.sparta.restaurant_search.repository

import com.sparta.restaurant_search.domain.dto.BestPlaceDto
import com.sparta.restaurant_search.domain.entity.DeliciousPlace
import com.sparta.restaurant_search.domain.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface PlaceRepository: JpaRepository<DeliciousPlace, Long> {
    fun findByUser(user: User): List<DeliciousPlace>

    fun findByUserAndPlaceId(user: User, placeId: Long): DeliciousPlace?

    fun findByPlaceName(placeName: String): DeliciousPlace?

    @Query("SELECT NEW com.sparta.restaurant_search.domain.dto.BestPlaceDto(p.placeId, p.placeName, p.placeAddress, p.placeUrl, COUNT(p.placeId)) " +
            "FROM DeliciousPlace p " +
            "GROUP BY p.placeId, p.placeName, p.placeAddress, p.placeUrl " +
            "ORDER BY COUNT(p.placeId) DESC")
    fun findBestPlaceList(): List<BestPlaceDto>
}