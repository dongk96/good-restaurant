package com.sparta.restaurant_search.security

import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.web.authentication.WebAuthenticationDetails
import java.io.Serializable

class JwtAuthenticationToken(
    private val userPrincipal: UserPrincipal,
    details: WebAuthenticationDetails
): AbstractAuthenticationToken(userPrincipal.authorities), Serializable {

    init {
        // JWT 검증이 됐을시에 바로 생성할 예정이므로, 생성시 authenticated를 true로 설정
        super.setAuthenticated(true)
        super.setDetails(details)
    }

    override fun getCredentials() = null

    override fun getPrincipal() = userPrincipal

    override fun isAuthenticated(): Boolean {
        return true
    }
}