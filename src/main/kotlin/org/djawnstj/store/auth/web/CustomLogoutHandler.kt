package org.djawnstj.store.auth.web

import com.fasterxml.jackson.databind.ObjectMapper
import org.djawnstj.store.auth.repository.TokenRepository
import org.djawnstj.store.auth.service.JwtService
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.djawnstj.store.common.api.Response
import org.djawnstj.store.common.exception.ErrorCode
import org.djawnstj.store.common.exception.GlobalException
import org.springframework.http.MediaType
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.logout.LogoutHandler
import org.springframework.stereotype.Service

@Service
class CustomLogoutHandler(
    private val jwtService: JwtService,
    private val tokenRepository: TokenRepository,
    private val objectMapper: ObjectMapper
): LogoutHandler {

    override fun logout(request: HttpServletRequest?, response: HttpServletResponse?, authentication: Authentication?) {
        val authHeader = request?.getHeader("Authorization")
        if (authHeader == null || !authHeader.startsWith("Bearer ")) return

        val jwt = authHeader.substring(7)
        val jti = jwtService.getJti(jwt)

        tokenRepository.findByJti(jti)?.let {
            tokenRepository.deleteByJti(jti)
            SecurityContextHolder.clearContext()
        } ?: run {
            GlobalException(ErrorCode.TOKEN_NOT_FOUND).let { ex ->
                val responseEntity = Response.error(ex.errorCode, ex.message)
                val jsonResponse = objectMapper.writeValueAsString(responseEntity)

                response?.contentType = "application/json"
                response?.characterEncoding = "UTF-8"
                response?.status = ex.errorCode.status.value()
                response?.writer?.write(jsonResponse)
            }
        }
    }

}