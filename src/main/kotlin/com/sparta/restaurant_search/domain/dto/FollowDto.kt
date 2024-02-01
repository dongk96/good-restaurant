package com.sparta.restaurant_search.domain.dto

import com.sparta.restaurant_search.domain.entity.Follow
import jakarta.validation.constraints.Email

data class FollowDto(
    val followId: Long?,
    val userId: Long?,
    val email: String,
    val statement: Boolean
) {
    companion object {
        fun fromFollowingEntities(followings: List<Follow>): List<FollowDto> {
            return followings.map {
                val dto = FollowDto(
                    it.followId,
                    it.host.userId,
                    it.host.email,
                    it.statement
                )

                dto
            }
        }

        fun fromFollowerEntities(followers: List<Follow>): List<FollowDto> {
            return followers.map {
                val dto = FollowDto (
                    it.followId,
                    it.follower.userId,
                    it.follower.email,
                    it.statement
                )

                dto
            }
        }
    }
}
