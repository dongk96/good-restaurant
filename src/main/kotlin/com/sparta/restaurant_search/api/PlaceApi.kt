package com.sparta.restaurant_search.api

import com.google.gson.JsonObject
import com.ip2location.spring.strategies.attribute.AttributeStrategy
import com.sparta.restaurant_search.domain.dto.BestPlaceDto
import com.sparta.restaurant_search.domain.dto.KeywordDto
import com.sparta.restaurant_search.domain.dto.PlaceDto
import com.sparta.restaurant_search.exception.KakaoApiException
import com.sparta.restaurant_search.exception.NaverApiException
import com.sparta.restaurant_search.security.UserPrincipal
import com.sparta.restaurant_search.service.PlaceService
import com.sparta.restaurant_search.web.reponse.ListResponse
import com.sparta.restaurant_search.web.reponse.SingleResponse
import com.sparta.restaurant_search.web.request.DeliciousPlaceRequest
import jakarta.servlet.http.HttpServletRequest
import lombok.RequiredArgsConstructor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequiredArgsConstructor
class PlaceApi(
    private val placeService: PlaceService
) {

    @GetMapping("/v2/places/search")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    fun findPlaces(
        @RequestParam search: String
    ): ResponseEntity<ListResponse<PlaceDto>> {
        val places = placeService.findPlaces(search)
        return if(places.isEmpty())
            ResponseEntity(
                ListResponse.fail(),
                HttpStatus.INTERNAL_SERVER_ERROR
            )
        else
            ResponseEntity(
                ListResponse.successOf(places),
                HttpStatus.OK)
    }

    @GetMapping("/v2/places/search-around")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    fun findPlacesAround(
        @RequestParam search: String,
        request: HttpServletRequest
    ): ResponseEntity<ListResponse<PlaceDto>> {

        val places = placeService.findPlacesAround(search, request.remoteAddr)
        return ResponseEntity(ListResponse.successOf(places), HttpStatus.OK)
    }

    @PostMapping("/v2/places/registrations")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    fun deliciousClick(
        @RequestBody request: DeliciousPlaceRequest,
        @AuthenticationPrincipal user: UserPrincipal
    ): ResponseEntity<SingleResponse<String>> {
        var msg = "맛집 등록 성공"
        val data = request.placeName
        val result = placeService.deliciousClick(request, user.id)
        if (!result) {
            msg = "맛집 등록 해제"
        }
        return ResponseEntity(SingleResponse.successOf(msg, data), HttpStatus.OK)
    }

    @GetMapping("/v3/places/my-lists")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    fun findMyPlaces(
        @AuthenticationPrincipal user: UserPrincipal
    ): ResponseEntity<ListResponse<PlaceDto>> {
        val deliciousPlaces = placeService.findMyPlaces(user.id)

        return ResponseEntity(ListResponse.successOf(deliciousPlaces), HttpStatus.OK)
    }

    @GetMapping("/v3/places/partner-lists/{hostId}")
    fun findFollowingUserPlaces(
        @PathVariable hostId: Long,
        @AuthenticationPrincipal user: UserPrincipal
    ): ResponseEntity<ListResponse<PlaceDto>> {
        val followingUserPlaces = placeService.findFollowingUserPlaces(hostId, user.id)

        return ResponseEntity(ListResponse.successOf(followingUserPlaces), HttpStatus.OK)
    }

    @GetMapping("/v3/places/priority-lists")
    fun findBestPlaces(): ResponseEntity<ListResponse<BestPlaceDto>> {
        val bestPlaces = placeService.findBestPlaces()

        return ResponseEntity(ListResponse.successOf(bestPlaces), HttpStatus.OK)
    }

    @GetMapping("/v1/keywords")
    fun findBestKeywords(): ResponseEntity<ListResponse<KeywordDto>> {
        val bestKeywords = placeService.findBestKeywords()

        return ResponseEntity(ListResponse.successOf(bestKeywords), HttpStatus.OK)
    }
}