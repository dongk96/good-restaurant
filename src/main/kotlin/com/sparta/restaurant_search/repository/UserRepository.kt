package com.sparta.restaurant_search.repository

import com.sparta.restaurant_search.domain.entity.User
import org.hibernate.annotations.Parameter
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserRepository: JpaRepository<User, Long> {
    fun existsByEmail(email: String): Boolean
    fun findByEmail(email: String): Optional<User>
}