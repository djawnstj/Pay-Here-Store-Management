package org.djawnstj.store.auth.web

import io.mockk.*
import jakarta.servlet.http.HttpServletRequest
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.djawnstj.store.auth.entity.AuthenticationCredentials
import org.djawnstj.store.auth.repository.TokenRepository
import org.djawnstj.store.auth.service.JwtService
import org.djawnstj.store.common.exception.ErrorCode
import org.djawnstj.store.common.exception.GlobalException
import org.junit.jupiter.api.Test
import org.springframework.security.core.context.SecurityContextHolder

class CustomLogoutHandlerTest {

    private val jwtService: JwtService = mockk()
    private val tokenRepository: TokenRepository = mockk()

    private val logoutHandler: CustomLogoutHandler = CustomLogoutHandler(jwtService, tokenRepository)

    @Test
    fun `로그아웃 요청을 받아 토큰을 삭제한다`() {
        // given
        val request: HttpServletRequest = mockk()
        val jwt = "token"
        val jti = "jti"
        val authenticationCredentials: AuthenticationCredentials = mockk()

        every { request.getHeader("Authorization") } returns "Bearer $jwt"
        every { jwtService.getJti(jwt) } returns jti
        every { tokenRepository.findByJti(jti) } returns authenticationCredentials
        justRun { tokenRepository.deleteByJti(jti) }
        mockkStatic(SecurityContextHolder::class)
        justRun { SecurityContextHolder.clearContext() }

        // when
        logoutHandler.logout(request, mockk(), mockk())

        // then
        verify(exactly = 1) { SecurityContextHolder.clearContext() }
    }

    @Test
    fun `로그아웃 요청에 포함된 토큰으로 토큰 저장소에서 토큰을 찾을 수 없을 때 예외를 반환한다`() {
        // given
        val request: HttpServletRequest = mockk()
        val jwt = "token"
        val jti = "jti"

        every { request.getHeader("Authorization") } returns "Bearer $jwt"
        every { jwtService.getJti(jwt) } returns jti
        every { tokenRepository.findByJti(jti) } returns null

        // when then
        assertThatThrownBy { logoutHandler.logout(request, mockk(), mockk()) }
            .isInstanceOf(GlobalException::class.java)
            .hasMessage(ErrorCode.TOKEN_NOT_FOUND.message)
    }

}