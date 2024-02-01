package com.sparta.restaurant_search.web.request

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern

data class SignInRequest (
    @field:NotBlank
    @field:Pattern(regexp = "^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,}$", message = "이메일 주소 형식이어야 합니다.")
    var email: String,

    @field:NotBlank
    @field:Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d).{4,10}\$", message = "비밀번호는 최소 4자 이상, 10자 이하이며 알파벳 대소문자(a~z, A~Z), 특수문자로 구성되어야 합니다.")
    var password: String
)
