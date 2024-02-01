package com.sparta.restaurant_search.exception

import com.sparta.restaurant_search.web.reponse.ApiResponseCode

class AcceptDeniedException(override var message: String): BaseException() {
    override var code: ApiResponseCode = ApiResponseCode.NOT_ACCEPTABLE
}