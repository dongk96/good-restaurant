package com.sparta.restaurant_search.repository

import com.sparta.restaurant_search.domain.entity.Promise
import com.sparta.restaurant_search.domain.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface PromiseRepository: JpaRepository<Promise, Long> {

    @Query("select p from Promise p where p.respondent = :respondent AND p.statement = true")
    fun findReceiveList(respondent: User): List<Promise>

    @Query("select p from Promise p where p.requester = :requester")
    fun findRequestList(requester: User): List<Promise>
}