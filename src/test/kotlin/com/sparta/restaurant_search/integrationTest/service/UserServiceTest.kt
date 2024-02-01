package com.sparta.restaurant_search.integrationTest.service

import com.sparta.restaurant_search.exception.BadRequestException
import com.sparta.restaurant_search.integrationTest.IntegrationTest
import com.sparta.restaurant_search.service.AuthService
import com.sparta.restaurant_search.web.request.SignInRequest
import org.assertj.core.api.AssertionsForClassTypes.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired

class UserServiceTest(
    @Autowired
    private val userService: AuthService
): IntegrationTest() {
    @Test
    fun 아이디_오류() {
        val request = SignInRequest("dldlehdrbrb@naver.com", "abcdefg")

        assertThrows<BadRequestException>("아이디가 일치하지 않습니다.") {
            userService.signIn(request.email, request.password)
        }
    }

    @Test
    fun 비밀번호_오류() {
        val request = SignInRequest("dldlehdrbrb@gmail.com", "abcd1234")

        assertThrows<BadRequestException>("비밀번호가 틀렸습니다.") {
            userService.signIn(request.email, request.password)
        }
    }

    @Test
    fun 로그인_성공() {
        val request = SignInRequest("dldlehdrbrb@gmail.com", "ldg247612!")
        val token = userService.signIn(request.email, request.password)

        assertThat(token).isNotNull()
    }
}