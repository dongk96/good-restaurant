package com.sparta.restaurant_search.domain.entity

import com.sparta.restaurant_search.enum.UserRole
import jakarta.persistence.*

@Entity
@Table(name = "users")
class User(email: String, password: String, role: UserRole) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val userId: Long? = null

    @Column(name = "e-mail", unique = true)
    val email: String = email

    @Column(name = "password")
    var password: String = password

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    val role: UserRole = role

    @OneToMany(mappedBy = "user", cascade = [CascadeType.REMOVE])
    var deliciousPlaceList: MutableList<DeliciousPlace> = mutableListOf()

    @OneToMany(mappedBy = "host", cascade = [CascadeType.REMOVE])
    var followingList: MutableList<Follow>? = mutableListOf()

    @OneToMany(mappedBy = "host", cascade = [CascadeType.REMOVE])
    var followerList: MutableList<Follow>? = mutableListOf()

    @OneToMany(mappedBy = "requester", cascade = [CascadeType.REMOVE])
    var joinRequestList: MutableList<Promise> = mutableListOf()

    @OneToMany(mappedBy = "respondent", cascade = [CascadeType.REMOVE])
    var joinReceiveList: MutableList<Promise> = mutableListOf()

    fun addPlace(deliciousPlace: DeliciousPlace) {
        this.deliciousPlaceList.add(deliciousPlace)
    }

    fun addFollowing(follow: Follow) {
        this.followingList?.add(follow)
    }

    fun addFollower(follow: Follow) {
        this.followerList?.add(follow)
    }

    fun addPromiseRequest(promise: Promise) {
        this.joinRequestList.add(promise)
    }

    fun addPromiseResponse(promise: Promise) {
        this.joinReceiveList.add(promise)
    }
}