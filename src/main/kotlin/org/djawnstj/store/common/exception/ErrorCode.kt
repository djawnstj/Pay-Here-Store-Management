package org.djawnstj.store.common.exception

import org.springframework.http.HttpStatus

enum class ErrorCode(
    val status: HttpStatus,
    val message: String,
    val code: String,
) {

    // SECURITY
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "토큰이 만료되었습니다.", "SE0001"),
    MISS_MATCH_TOKEN(HttpStatus.UNAUTHORIZED, "토큰이 일치하지 않습니다.", "SE0002"),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "토큰이 유효하지 않습니다.", "SE0003"),
    INVALID_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "액세스 토큰이 유효하지 않습니다.", "SE0004"),
    INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "리프레시 토큰이 유효하지 않습니다.", "SE0005"),
    TOKEN_NOT_FOUND(HttpStatus.NOT_FOUND, "토큰을 찾을 수 없습니다.", "SE0006"),
    EMPTY_CLAIM(HttpStatus.UNAUTHORIZED, "CLAIM 정보가 비어있습니다.", "SE0007"),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "토큰 인증에 실패하였습니다.", "SE0008"),
    FORBIDDEN(HttpStatus.FORBIDDEN, "접근 권한이 없습니다.", "SE0009"),
    INVALID_TOKEN_REISSUE_REQUEST(HttpStatus.BAD_REQUEST, "토큰을 재발급 할 수 없습니다.", "SE0010"),

    // MEMBER
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "일치하는 회원 정보를 찾을 수 없습니다.", "ME0001"),
    DUPLICATED_REGISTER_PHONE_NUMBER(HttpStatus.CONFLICT, "이미 존재하는 휴대폰 번호 입니다.", "ME0002"),

    // PRODUCT
    DUPLICATED_REGISTER_PRODUCT_CATEGORY_NAME(HttpStatus.CONFLICT, "이미 존재하는 카테고리 입니다.", "PE0001"),
    PRODUCT_CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "카테고리를 찾을 수 없습니다.", "PE0002"),
    PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "상품을 찾을 수 없습니다.", "PE0003"),

    // INPUT VALIDATION
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "입력값이 올바르지 않습니다.", "VE0001"),
    NO_CONTENT_HTTP_BODY(HttpStatus.BAD_REQUEST, "정상적인 요청 본문이 아닙니다.", "VE0002"),

    // REQUEST ERROR
    NOT_SUPPORTED_METHOD(HttpStatus.METHOD_NOT_ALLOWED, "정상적인 요청이 아닙니다.", "RE0001"),

    // COMMON
    INTERNAL_SEVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "관리자에게 문의 바랍니다.", "IE0001"),
    ;

}