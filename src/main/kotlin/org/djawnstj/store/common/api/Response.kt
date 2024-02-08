package org.djawnstj.store.common.api

import org.djawnstj.store.common.exception.ErrorCode
import org.springframework.http.HttpStatus

data class Response<T>(
    val meta: Meta,
    val data: T?
) {
    data class Meta(
        val code: Int,
        val message: String
    )

    companion object {
        fun <T> success(status: HttpStatus, data: T): Response<T> =
            Response(
                meta = Meta(
                    code = status.value(),
                    message = status.reasonPhrase
                ), data = data)

        fun error(errorCode: ErrorCode) =
            Response(
                meta = Meta(
                    code = errorCode.status.value(),
                    message = errorCode.message
                ), data = null)
    }

}