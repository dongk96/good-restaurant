package com.sparta.restaurant_search.exception

import com.sparta.restaurant_search.web.reponse.ApiResponseCode

class NaverApiException(override var message: String): BaseException() {
    override var code: ApiResponseCode = ApiResponseCode.INTERNAL_SERVER_ERROR
}