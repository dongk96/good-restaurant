package com.sparta.restaurant_search.repository

import com.sparta.restaurant_search.domain.entity.Follow
import com.sparta.restaurant_search.domain.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface FollowRepository: JpaRepository<Follow, Long> {

    fun findByFollowerAndHost(follower: User, host: User): Follow

    @Query("SELECT f FROM Follow f WHERE f.follower = :follower AND f.statement = true")
    fun findFollowingList(follower: User): List<Follow>

    @Query("SELECT f FROM Follow f WHERE f.host = :host AND f.statement = false")
    fun findFollowerListStandBy(host: User): List<Follow>

    @Query("SELECT f FROM Follow f WHERE f.host = :host AND f.statement = true")
    fun findFollowerListAccept(host: User): List<Follow>

}