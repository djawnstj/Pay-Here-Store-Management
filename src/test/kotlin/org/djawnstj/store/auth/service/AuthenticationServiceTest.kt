package org.djawnstj.store.auth.service

import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.djawnstj.store.auth.dto.refresh.TokenRefreshRequest
import org.djawnstj.store.auth.dto.signin.SignInRequest
import org.djawnstj.store.auth.entity.AuthenticationCredentials
import org.djawnstj.store.auth.repository.TokenRepository
import org.djawnstj.store.common.exception.ErrorCode
import org.djawnstj.store.common.exception.GlobalException
import org.djawnstj.store.member.entity.Member
import org.djawnstj.store.member.repository.MemberQueryRepository
import org.junit.jupiter.api.Test
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication

class AuthenticationServiceTest {

    private val tokenRepository: TokenRepository = mockk()
    private val memberQueryRepository: MemberQueryRepository = mockk()
    private val jwtService: JwtService = mockk()
    private val authenticationManager: AuthenticationManager = mockk()
    private val authService: AuthenticationService = AuthenticationService(tokenRepository, memberQueryRepository, jwtService, authenticationManager)

    @Test
    fun `유저의 phoneNumber 와 password 를 받아 로그인을 수행 후 토큰을 반환한다`() {
        // given
        val phoneNumber = "010-0000-0000"
        val password = "password"
        val request = SignInRequest(phoneNumber, password)

        val authentication: Authentication = mockk()
        val member: Member = mockk()
        val authenticationCredentials: AuthenticationCredentials = mockk()

        val accessToken = "accessToken"
        val refreshToken = "refreshToken"

        every { authenticationManager.authenticate(any<UsernamePasswordAuthenticationToken>()) } returns authentication
        every { authentication.principal } returns member
        every { jwtService.generateAuthenticationCredentials(member) } returns authenticationCredentials
        every { tokenRepository.save(authenticationCredentials) } returns authenticationCredentials
        every { authenticationCredentials.accessToken } returns accessToken
        every { authenticationCredentials.refreshToken } returns refreshToken

        // when
        val response = authService.signIn(request)

        // then
        assertThat(response)
            .extracting("accessToken", "refreshToken")
            .contains(accessToken, refreshToken)
    }

    @Test
    fun `refreshToken 을 받아 토큰을 재발급 한다`() {
        // given
        val originAccessToken = "originAccessToken"
        val originRefreshToken = "originRefreshToken"
        val request = TokenRefreshRequest(originRefreshToken)
        val phoneNumber = "010-0000-0000"
        val jti = "jti"
        val member: Member = mockk()
        val originAuthenticationCredentials: AuthenticationCredentials = mockk()
        val newAuthenticationCredentials = mockk<AuthenticationCredentials>()

        val newAccessToken = "newAccessToken"
        val newRefreshToken = "newRefreshToken"

        every { jwtService.getUsername(originRefreshToken) } returns phoneNumber
        every { jwtService.getJti(originRefreshToken) } returns jti
        every { memberQueryRepository.findByPhoneNumber(phoneNumber) } returns member
        every { tokenRepository.findByJti(jti) } returns originAuthenticationCredentials
        every { originAuthenticationCredentials.refreshToken } returns originRefreshToken
        every { jwtService.isTokenValid(originRefreshToken, member) } returns true
        every { originAuthenticationCredentials.accessToken } returns originAccessToken
        every { jwtService.checkTokenExpiredByTokenString(originAccessToken) } returns true
        every { member.phoneNumber } returns phoneNumber
        every { jwtService.generateAuthenticationCredentials(member) } returns newAuthenticationCredentials
        every { tokenRepository.save(newAuthenticationCredentials) } returns newAuthenticationCredentials
        every { newAuthenticationCredentials.accessToken } returns newAccessToken
        every { newAuthenticationCredentials.refreshToken } returns newRefreshToken

        // when
        val result = authService.refreshToken(request)

        // then
        assertThat(result)
            .extracting("accessToken", "refreshToken")
            .contains(newAccessToken, newRefreshToken)
    }

    @Test
    fun `토큰 재발급 시 전달 받은 refreshToken 의 username 으로 찾은 UserDetails 가 없다면 예외를 반환한다`() {
        // given
        val originRefreshToken = "originRefreshToken"
        val request = TokenRefreshRequest(originRefreshToken)
        val phoneNumber = "010-0000-0000"
        val jti = "jti"

        every { jwtService.getUsername(originRefreshToken) } returns phoneNumber
        every { jwtService.getJti(originRefreshToken) } returns jti
        every { memberQueryRepository.findByPhoneNumber(phoneNumber) } returns null

        // when then
        assertThatThrownBy { authService.refreshToken(request) }
            .isInstanceOf(GlobalException::class.java)
            .hasMessage(ErrorCode.MEMBER_NOT_FOUND.message)
    }

    @Test
    fun `토큰 재발급 시 전달 받은 refreshToken 의 jti 으로 찾은 토큰이 없다면 예외를 반환한다`() {
        // given
        val originRefreshToken = "originRefreshToken"
        val request = TokenRefreshRequest(originRefreshToken)
        val phoneNumber = "010-0000-0000"
        val jti = "jti"
        val member: Member = mockk()

        every { jwtService.getUsername(originRefreshToken) } returns phoneNumber
        every { jwtService.getJti(originRefreshToken) } returns jti
        every { memberQueryRepository.findByPhoneNumber(phoneNumber) } returns member
        every { tokenRepository.findByJti(jti) } returns null

        // when then
        assertThatThrownBy { authService.refreshToken(request) }
            .isInstanceOf(GlobalException::class.java)
            .hasMessage(ErrorCode.TOKEN_NOT_FOUND.message)
    }

    @Test
    fun `토큰 재발급 시 전달 받은 refreshToken 과 토큰에 포함된 jti 로 저장소에서 찾은 refreshToken 이 다른 경우 예외를 반환한다`() {
        // given
        val originRefreshToken = "originRefreshToken"
        val request = TokenRefreshRequest(originRefreshToken)
        val phoneNumber = "010-0000-0000"
        val jti = "jti"
        val member: Member = mockk()
        val originAuthenticationCredentials: AuthenticationCredentials = mockk()

        every { jwtService.getUsername(originRefreshToken) } returns phoneNumber
        every { jwtService.getJti(originRefreshToken) } returns jti
        every { memberQueryRepository.findByPhoneNumber(phoneNumber) } returns member
        every { tokenRepository.findByJti(jti) } returns originAuthenticationCredentials
        every { originAuthenticationCredentials.refreshToken } returns "refreshToken"

        // when then
        assertThatThrownBy { authService.refreshToken(request) }
            .isInstanceOf(GlobalException::class.java)
            .hasMessage(ErrorCode.MISS_MATCH_TOKEN.message)
    }

    @Test
    fun `토큰 재발급 시 전달 받은 refreshToken 이 유효하지 않다면 예외를 반환한다`() {
        // given
        val originRefreshToken = "originRefreshToken"
        val request = TokenRefreshRequest(originRefreshToken)
        val phoneNumber = "010-0000-0000"
        val jti = "jti"
        val member: Member = mockk()
        val authenticationCredentials: AuthenticationCredentials = mockk()

        every { jwtService.getUsername(originRefreshToken) } returns phoneNumber
        every { jwtService.getJti(originRefreshToken) } returns jti
        every { memberQueryRepository.findByPhoneNumber(phoneNumber) } returns member
        every { tokenRepository.findByJti(jti) } returns authenticationCredentials
        every { authenticationCredentials.refreshToken } returns originRefreshToken
        every { jwtService.isTokenValid(originRefreshToken, member) } returns false

        // when then
        assertThatThrownBy { authService.refreshToken(request) }
            .isInstanceOf(GlobalException::class.java)
            .hasMessage(ErrorCode.INVALID_REFRESH_TOKEN.message)
    }

    @Test
    fun `토큰 재발급 시 전달 받은 토큰의 jti 로 찾은 accessToken 의 만료시간이 지나지 않았다면 토큰 저장소에서 토큰을 삭제하고 예외를 반환한다`() {
        // given
        val originAccessToken = "originAccessToken"
        val originRefreshToken = "originRefreshToken"
        val request = TokenRefreshRequest(originRefreshToken)
        val phoneNumber = "010-0000-0000"
        val jti = "jti"
        val member: Member = mockk()
        val originAuthenticationCredentials: AuthenticationCredentials = mockk()

        every { jwtService.getUsername(originRefreshToken) } returns phoneNumber
        every { jwtService.getJti(originRefreshToken) } returns jti
        every { memberQueryRepository.findByPhoneNumber(phoneNumber) } returns member
        every { tokenRepository.findByJti(jti) } returns originAuthenticationCredentials
        every { originAuthenticationCredentials.refreshToken } returns originRefreshToken
        every { jwtService.isTokenValid(originRefreshToken, member) } returns true
        every { originAuthenticationCredentials.accessToken } returns originAccessToken
        every { jwtService.checkTokenExpiredByTokenString(originAccessToken) } returns false
        justRun { tokenRepository.deleteByJti(jti) }

        // when then
        assertThatThrownBy { authService.refreshToken(request) }
            .isInstanceOf(GlobalException::class.java)
            .hasMessage(ErrorCode.INVALID_TOKEN_REISSUE_REQUEST.message)
        verify(atMost = 1) { tokenRepository.deleteByJti(jti) }
    }

}