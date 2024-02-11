package org.djawnstj.store.auth.service

import org.assertj.core.api.Assertions.assertThat
import org.djawnstj.store.IntegrationTestSupport
import org.djawnstj.store.auth.config.JwtProperties
import org.djawnstj.store.auth.dto.refresh.TokenRefreshRequest
import org.djawnstj.store.auth.dto.signin.LogInRequest
import org.djawnstj.store.auth.entity.AuthenticationCredentials
import org.djawnstj.store.auth.repository.TokenRepository
import org.djawnstj.store.member.entity.Member
import org.djawnstj.store.member.repository.MemberJpaRepository
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.password.PasswordEncoder
import java.util.*

class AuthenticationServiceIntegrationTest: IntegrationTestSupport() {

    @Autowired
    private lateinit var authService: AuthenticationService

    @Autowired
    private lateinit var passwordEncoder: PasswordEncoder
    @Autowired
    private lateinit var memberJpaRepository: MemberJpaRepository
    @Autowired
    private lateinit var tokenProvider: TokenProvider
    @Autowired
    private lateinit var jwtProperties: JwtProperties
    @Autowired
    private lateinit var tokenRepository: TokenRepository

    @AfterEach
    fun tearDown() {
        memberJpaRepository.deleteAll()
        tokenRepository.deleteAll()
    }

    @Test
    fun `회원 가입 된 유저의 phoneNumber 와 password 를 받아 로그인을 수행 후 토큰을 반환한다`() {
        // given
        val phoneNumber = "010-0000-0000"
        val password = "password"
        memberJpaRepository.save(Member(phoneNumber, passwordEncoder.encode(password), "name"))

        val request = LogInRequest(phoneNumber, password)

        // when
        val response = authService.logIn(request)

        // then
        assertThat(response.accessToken).isNotBlank()
        assertThat(response.refreshToken).isNotBlank()
    }

    @Test
    fun `accessToken 이 만료됐을 시 refreshToken 을 받아 토큰을 재발급 한다`() {
        // given
        val phoneNumber = "010-0000-0000"
        val password = "password"
        val member = memberJpaRepository.save(Member(phoneNumber, passwordEncoder.encode(password), "name"))

        val jti = UUID.randomUUID().toString()

        val accessToken = tokenProvider.buildToken(member, jti, 0L)
        val refreshToken = tokenProvider.buildToken(member, jti, jwtProperties.refreshTokenExpiration)

        val authenticationCredentials = tokenRepository.save(AuthenticationCredentials(jti, accessToken, refreshToken))

        val request = TokenRefreshRequest(authenticationCredentials.refreshToken)

        // when
        val response = authService.refreshToken(request)

        // then
        assertThat(response.accessToken).isNotEqualTo(
            AuthenticationCredentials(
                jti,
                accessToken,
                refreshToken,
                member.id
            ).accessToken
        )
        assertThat(response.refreshToken).isNotEqualTo(
            AuthenticationCredentials(
                jti,
                accessToken,
                refreshToken,
                member.id
            ).refreshToken
        )
    }

}