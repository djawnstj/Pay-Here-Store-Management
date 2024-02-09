package org.djawnstj.store.common.exception

import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.MalformedJwtException
import io.jsonwebtoken.security.SignatureException
import org.djawnstj.store.common.api.Response
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.security.authentication.InsufficientAuthenticationException
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import javax.naming.AuthenticationException

@RestControllerAdvice
class GlobalRestControllerAdvice {

    @ExceptionHandler(GlobalException::class)
    fun handleGlobalServerException(e: GlobalException): ResponseEntity<Response<*>> =
        ResponseEntity(Response.error(e.errorCode), e.errorCode.status)

    @ExceptionHandler(AuthenticationException::class)
    fun handleAuthenticationException(e: AuthenticationException): ResponseEntity<Response<*>> =
        GlobalException(cause = e, errorCode = ErrorCode.UNAUTHORIZED)
            .let { ex -> ResponseEntity(Response.error(ex.errorCode), ex.errorCode.status) }

    @ExceptionHandler(InsufficientAuthenticationException::class)
    fun handleInsufficientAuthenticationException(e: InsufficientAuthenticationException): ResponseEntity<Response<*>> =
        GlobalException(cause = e, errorCode = ErrorCode.UNAUTHORIZED)
            .let { ex -> ResponseEntity(Response.error(ex.errorCode), ex.errorCode.status) }

    @ExceptionHandler(AccessDeniedException::class)
    fun handleAccessDeniedException(e: AccessDeniedException): ResponseEntity<Response<*>> =
        GlobalException(cause = e, errorCode = ErrorCode.FORBIDDEN)
            .let { ex -> ResponseEntity(Response.error(ex.errorCode), ex.errorCode.status) }

    @ExceptionHandler(SignatureException::class)
    fun handleSignatureException(e: SignatureException) =
        GlobalException(cause = e, errorCode = ErrorCode.INVALID_TOKEN)
            .let { ex -> ResponseEntity(Response.error(ex.errorCode), ex.errorCode.status) }

    @ExceptionHandler(MalformedJwtException::class)
    fun handleMalformedJwtException(e: MalformedJwtException) =
        GlobalException(cause = e, errorCode = ErrorCode.INVALID_TOKEN)
            .let { ex -> ResponseEntity(Response.error(ex.errorCode), ex.errorCode.status) }

    @ExceptionHandler(ExpiredJwtException::class)
    fun handleExpiredJwtException(e: ExpiredJwtException) =
        GlobalException(cause = e, errorCode = ErrorCode.EXPIRED_TOKEN)
            .let { ex -> ResponseEntity(Response.error(ex.errorCode), ex.errorCode.status) }


    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleMethodArgumentNotValidException(e: MethodArgumentNotValidException): ResponseEntity<Response<*>> =
        GlobalException(ErrorCode.INVALID_INPUT_VALUE, ErrorCode.INVALID_INPUT_VALUE.message, e,)
            .let { ex -> ResponseEntity(Response.error(ex.errorCode), ex.errorCode.status) }

    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handleHttpMessageNotReadableException(e: HttpMessageNotReadableException): ResponseEntity<Response<*>> =
        GlobalException(ErrorCode.NO_CONTENT_HTTP_BODY, ErrorCode.NO_CONTENT_HTTP_BODY.message, e)
            .let { ex -> ResponseEntity(Response.error(ex.errorCode), ex.errorCode.status) }

    @ExceptionHandler(HttpRequestMethodNotSupportedException::class)
    fun handleHttpRequestMethodNotSupportedException(e: HttpRequestMethodNotSupportedException): ResponseEntity<Response<*>> =
        GlobalException(ErrorCode.NOT_SUPPORTED_METHOD, ErrorCode.NOT_SUPPORTED_METHOD.message, e)
            .let { ex -> ResponseEntity(Response.error(ex.errorCode), ex.errorCode.status) }

    @ExceptionHandler(Throwable::class)
    fun handleThrowable(t: Throwable): ResponseEntity<Response<*>> =
        GlobalException(cause = t, errorCode = ErrorCode.INTERNAL_SEVER_ERROR)
            .let { ex -> ResponseEntity(Response.error(ex.errorCode), ex.errorCode.status) }

}