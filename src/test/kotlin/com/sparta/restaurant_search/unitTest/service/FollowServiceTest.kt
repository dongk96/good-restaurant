package com.sparta.restaurant_search.unitTest.service

import com.sparta.restaurant_search.repository.FollowRepository
import com.sparta.restaurant_search.repository.UserRepository
import com.sparta.restaurant_search.service.FollowService
import io.mockk.mockk
import org.junit.jupiter.api.Test

class FollowServiceTest {
    private val followRepository: FollowRepository = mockk()
    private val userRepository: UserRepository = mockk()
    private val followService: FollowService = FollowService(followRepository, userRepository)

    @Test
    fun 팔로우_신청() {

    }
}