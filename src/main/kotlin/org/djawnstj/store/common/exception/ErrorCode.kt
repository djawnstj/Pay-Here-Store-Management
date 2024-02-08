package org.djawnstj.store.common.exception

import org.springframework.http.HttpStatus

enum class ErrorCode(
    val status: HttpStatus,
    val message: String,
    val code: String,
) {

    // INPUT VALIDATION
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "입력값이 올바르지 않습니다.", "VE0001"),
    NO_CONTENT_HTTP_BODY(HttpStatus.BAD_REQUEST, "정상적인 요청 본문이 아닙니다.", "VE0002"),

    // REQUEST ERROR
    NOT_SUPPORTED_METHOD(HttpStatus.METHOD_NOT_ALLOWED, "정상적인 요청이 아닙니다.", "RE0001"),

    // COMMON
    INTERNAL_SEVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "관리자에게 문의 바랍니다.", "IE0001"),
    ;

}