package com.sparta.restaurant_search.api

import com.sparta.restaurant_search.service.AuthService
import com.sparta.restaurant_search.web.reponse.SingleResponse
import com.sparta.restaurant_search.web.request.SignInRequest
import com.sparta.restaurant_search.web.request.SignUpRequest
import jakarta.validation.Valid
import lombok.RequiredArgsConstructor
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
class AuthApi (
    private val authService: AuthService
) {
    @PostMapping("/sign-up")
    fun signUp(
        @Valid @RequestBody request: SignUpRequest
    ): ResponseEntity<SingleResponse<String>> {
        authService.signUp(request.email, request.password, request.role)

        return ResponseEntity(
            SingleResponse.success("회원가입 성공!!"),
            HttpStatus.OK
        )
    }

    @PostMapping("/sign-in")
    fun singIn(
        @Valid @RequestBody request: SignInRequest
    ): ResponseEntity<SingleResponse<String>> {
        val token = authService.signIn(request.email, request.password)

        return ResponseEntity(
            SingleResponse.successOf("로그인 성공!!", token),
            HttpStatus.OK
        )
    }
}