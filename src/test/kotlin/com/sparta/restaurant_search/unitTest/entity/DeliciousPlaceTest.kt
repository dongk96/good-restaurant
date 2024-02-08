package com.sparta.restaurant_search.unitTest.entity

import com.sparta.restaurant_search.domain.entity.DeliciousPlace
import com.sparta.restaurant_search.domain.entity.Promise
import com.sparta.restaurant_search.domain.entity.User
import com.sparta.restaurant_search.enum.UserRole
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertTrue

class DeliciousPlaceTest {
    @Test
    fun 약속_생성() {
        val user1 = User("dldlehdrbrb@gmail.com", "ldg1234!", UserRole.ADMIN)
        val user2 = User("dldlehdrbrb@naver.com", "ldg5678!", UserRole.USER)

        val place = DeliciousPlace(user1, 1, "맥도날드", "서울시 양천구 목동", "www.MCDonald.com")
        val promise = Promise(place, user1, user2)

        place.addPromise(promise)
        assertTrue(place.promiseList.contains(promise))
    }
}