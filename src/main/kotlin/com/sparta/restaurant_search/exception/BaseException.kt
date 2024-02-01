package com.sparta.restaurant_search.exception

import com.sparta.restaurant_search.web.reponse.ApiResponseCode

abstract class BaseException: RuntimeException() {

    open lateinit var code: ApiResponseCode
    override lateinit var message: String

}