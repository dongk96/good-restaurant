package com.sparta.restaurant_search.domain.entity

import jakarta.persistence.*

@Entity
@Table(name = "places")
class DeliciousPlace(
    user: User,
    placeId: Long,
    placeName: String,
    placeAddress: String,
    placeUrl: String) {

    init {
        user.addPlace(this)
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val deliciousPlaceId: Long? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user-id")
    var user: User = user

    @Column(name = "place-kakao-id")
    var placeId: Long? = placeId

    @Column(name = "place-name")
    val placeName: String = placeName

    @Column(name = "place-address")
    val placeAddress: String = placeAddress

    @Column(name = "place-url")
    val placeUrl: String = placeUrl

//    @Column(name = "regis-num")
//    var regisNum: Long = 0

    @OneToMany(mappedBy = "place", cascade = [CascadeType.REMOVE])
    var promiseList: MutableList<Promise> = mutableListOf()

    fun addPromise(promise: Promise) {
        this.promiseList.add(promise)
    }

//    fun addRegistrationNum(regisNumNow: Long) {
//        this.regisNum = regisNumNow + 1
//    }
//
//    fun minusRegistrationNum(regisNumNow: Long) {
//        this.regisNum = regisNumNow - 1
//    }
}