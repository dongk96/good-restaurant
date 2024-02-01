package com.sparta.restaurant_search.api

import com.sparta.restaurant_search.domain.dto.FollowDto
import com.sparta.restaurant_search.security.JwtAuthenticationToken
import com.sparta.restaurant_search.security.UserPrincipal
import com.sparta.restaurant_search.service.FollowService
import com.sparta.restaurant_search.web.reponse.ListResponse
import com.sparta.restaurant_search.web.reponse.SingleResponse
import lombok.RequiredArgsConstructor
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v2/follow")
@RequiredArgsConstructor
class FollowApi(
    private val followService: FollowService
) {

    @PostMapping("/{hostId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    fun follow(
        @PathVariable hostId: Long,
        @AuthenticationPrincipal follower: UserPrincipal
    ): ResponseEntity<SingleResponse<String>> {

        followService.follow(hostId, follower.id)
        return ResponseEntity(SingleResponse.success("팔로우 신청 성공!"), HttpStatus.OK)
    }

    @GetMapping("/following-list")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    fun followingList(
        @AuthenticationPrincipal follower: UserPrincipal
    ): ResponseEntity<ListResponse<FollowDto>> {
        val followingList = followService.followingList(follower.id)

        return ResponseEntity(ListResponse.successOf(followingList), HttpStatus.OK)
    }

    @GetMapping("/standBy-follower-list")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    fun standByFollowerList(
        @AuthenticationPrincipal host: UserPrincipal
    ): ResponseEntity<ListResponse<FollowDto>> {
        val followerList = followService.standByFollowerList(host.id)

        return ResponseEntity(ListResponse.successOf(followerList), HttpStatus.OK)
    }

    @GetMapping("/accept-follower-list")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    fun acceptFollowerList(
        @AuthenticationPrincipal host: UserPrincipal
    ): ResponseEntity<ListResponse<FollowDto>> {
        val followerList = followService.acceptFollowerList(host.id)

        return ResponseEntity(ListResponse.successOf(followerList), HttpStatus.OK)
    }

    @PutMapping("/accept/{followerId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    fun followAccept(
        @PathVariable followerId: Long,
    ): ResponseEntity<SingleResponse<String>> {
        followService.followAccept(followerId)

        return ResponseEntity(SingleResponse.success("팔로우 승인 완료!"), HttpStatus.OK)
    }


    @DeleteMapping("/delete/{followerId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    fun followDelete(
        @PathVariable followerId: Long,
        @AuthenticationPrincipal host: UserPrincipal
    ): ResponseEntity<SingleResponse<String>> {
        followService.followDelete(followerId, host.id)

        return ResponseEntity(SingleResponse.success("팔로우 취소 성공!"), HttpStatus.OK)
    }
}