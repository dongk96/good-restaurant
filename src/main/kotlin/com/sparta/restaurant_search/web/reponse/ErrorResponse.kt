package com.sparta.restaurant_search.web.reponse

import lombok.Getter
import lombok.Setter

@Getter
@Setter
data class ErrorResponse(
    val code: ApiResponseCode? = null,
    val message: String? = null
)
