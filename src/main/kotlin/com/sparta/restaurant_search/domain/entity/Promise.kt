package com.sparta.restaurant_search.domain.entity

import jakarta.persistence.*

@Entity
@Table(name = "promises")
class Promise(
    deliciousPlace: DeliciousPlace,
    requester: User,
    respondent: User
) {
    init {
        deliciousPlace.addPromise(this)
        requester.addPromiseRequest(this)
        respondent.addPromiseResponse(this)
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val promiseId: Long? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_id")
    val place: DeliciousPlace = deliciousPlace

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requester_id")
    val requester: User = requester

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "respondent_id")
    val respondent:User = respondent

    @Column(name = "state")
    var statement: Boolean = false

    fun updateState() {
        this.statement = true
    }
}