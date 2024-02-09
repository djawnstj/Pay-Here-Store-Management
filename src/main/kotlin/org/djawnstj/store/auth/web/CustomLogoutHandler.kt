package org.djawnstj.store.auth.web

import org.djawnstj.store.auth.repository.TokenRepository
import org.djawnstj.store.auth.service.JwtService
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.djawnstj.store.common.exception.ErrorCode
import org.djawnstj.store.common.exception.GlobalException
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.logout.LogoutHandler
import org.springframework.stereotype.Service

@Service
class CustomLogoutHandler(
    private val jwtService: JwtService,
    private val tokenRepository: TokenRepository
): LogoutHandler {

    override fun logout(request: HttpServletRequest?, response: HttpServletResponse?, authentication: Authentication?) {
        val authHeader = request?.getHeader("Authorization")
        if (authHeader == null || !authHeader.startsWith("Bearer ")) return

        val jwt = authHeader.substring(7)
        val jti = jwtService.getJti(jwt)

        tokenRepository.findByJti(jti)?.let {
            tokenRepository.deleteByJti(jti)
            SecurityContextHolder.clearContext()
        } ?: throw GlobalException(ErrorCode.TOKEN_NOT_FOUND)
    }

}