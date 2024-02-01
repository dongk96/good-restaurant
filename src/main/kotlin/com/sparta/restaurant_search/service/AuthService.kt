package com.sparta.restaurant_search.service

import com.sparta.restaurant_search.domain.entity.User
import com.sparta.restaurant_search.enum.UserRole
import com.sparta.restaurant_search.exception.BadRequestException
import com.sparta.restaurant_search.exception.DuplicateException
import com.sparta.restaurant_search.exception.NotFoundException
import com.sparta.restaurant_search.repository.UserRepository
import com.sparta.restaurant_search.security.JwtPlugin
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtPlugin: JwtPlugin
) {
    fun signUp(email: String, password: String, role: UserRole) {
        val existCheck = userRepository.existsByEmail(email)
        if(existCheck) {
            throw DuplicateException("중복된 e-mail 입니다.")
        }

        val user = User(email, passwordEncoder.encode(password), role)

        userRepository.save(user)
    }

    fun signIn(email: String, password: String): String {
        val user = userRepository.findByEmail(email).orElseThrow {
            NotFoundException("e-mail 또는 password가 틀렸습니다.")
        }

        if(!passwordEncoder.matches(password, user.password)) {
            throw BadRequestException("e-mail 또는 password가 틀렸습니다.")
        }

        return jwtPlugin.generateAccessToken(
            user.userId.toString(),
            user.email,
            user.role)
    }
}