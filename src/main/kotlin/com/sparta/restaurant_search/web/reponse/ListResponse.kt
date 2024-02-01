package com.sparta.restaurant_search.web.reponse

import lombok.Getter
import lombok.Setter

@Getter
@Setter
data class ListResponse<T>(
    var code: String? = null,
    var message: String? = null,
    var data: List<T>? = null
) {
    companion object {
        fun <T> of(code: String?, message: String?, data: List<T>?): ListResponse<T> {
            return ListResponse(code, message, data)
        }

        fun <T> of(code: String?, message: String?): ListResponse<T> {
            return of(code, message, null)
        }

        fun <T> successOf(data: List<T>): ListResponse<T> {
            return of("S-1", "성공", data)
        }

        fun <T> success(): ListResponse<T> {
            return of("S-1", "성공", null)
        }

        fun <T> failOf(data: List<T>): ListResponse<T> {
            return of("F-1", "실패", data)
        }
        fun <T> fail(): ListResponse<T> {
            return of("F-1", "실패")
        }
    }
}
