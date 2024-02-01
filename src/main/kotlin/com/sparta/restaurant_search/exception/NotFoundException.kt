package com.sparta.restaurant_search.exception

import com.sparta.restaurant_search.web.reponse.ApiResponseCode

class NotFoundException(override var message: String): BaseException() {
    override var code: ApiResponseCode = ApiResponseCode.NOT_FOUND
}
