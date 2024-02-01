package com.sparta.restaurant_search.domain.entity

import jakarta.persistence.*

@Entity
@Table(name = "follow")
class Follow(follower: User, host: User) {
    init {
        follower.addFollowing(this)
        host.addFollower(this)
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val followId: Long? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "host_id")
    var host: User = host

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "follower_id")
    var follower: User = follower

    @Column(name = "state")
    var statement: Boolean = false

    fun updateState() {
        this.statement = true
    }
}