package org.djawnstj.store.auth.repository

import org.assertj.core.api.Assertions.assertThat
import org.djawnstj.store.IntegrationTestSupport
import org.djawnstj.store.auth.entity.AuthenticationCredentials
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.util.*

class TokenRepositoryTest : IntegrationTestSupport() {

    @Autowired
    private lateinit var tokenRepository: TokenRepository

    @AfterEach
    fun tearDown() {
        tokenRepository.deleteAll()
    }

    @Test
    fun `TokenAuthenticationCredentials 를 받아서 토큰 정보를 저장한다`() {
        // given
        val authenticationCredentials = AuthenticationCredentials(UUID.randomUUID().toString(), "accessToken", "refreshToken")

        // when then
        tokenRepository.save(authenticationCredentials)
    }

    @Test
    fun `key 를 이용해 저장된 토큰을 조회한다`() {
        // given
        val authenticationCredentials =
            tokenRepository.save(AuthenticationCredentials(UUID.randomUUID().toString(), "accessToken", "refreshToken"))

        // when
        val result = tokenRepository.findByJti(authenticationCredentials.jti)

        // then
        assertThat(result).isNotNull
        assertThat(result!!)
            .extracting("accessToken", "refreshToken")
            .contains(authenticationCredentials.accessToken, authenticationCredentials.refreshToken)
    }

    @Test
    fun `key 를 이용해 토큰을 조회할 때, 저장된 토큰이 없으면 null 을 반환한다`() {
        // given
        val key = "key"

        // when
        val authenticationCredentials = tokenRepository.findByJti(key)

        // then
        assertThat(authenticationCredentials).isNull()
    }

    @Test
    fun `key 를 이용해 저장된 토큰을 삭제한다`() {
        // given
        val authenticationCredentials =
            tokenRepository.save(AuthenticationCredentials(UUID.randomUUID().toString(), "accessToken", "refreshToken"))

        // when
        tokenRepository.deleteByJti(authenticationCredentials.jti)

        // then
        val result = tokenRepository.findByJti(authenticationCredentials.jti)
        assertThat(result).isNull()
    }

    @Test
    fun `저장된 모든 토큰을 삭제한다`() {
        // given
        val authenticationCredentials1 = AuthenticationCredentials(UUID.randomUUID().toString(), "accessToken1", "refreshToken1")
        val authenticationCredentials2 = AuthenticationCredentials(UUID.randomUUID().toString(), "accessToken2", "refreshToken2")
        val authenticationCredentials3 = AuthenticationCredentials(UUID.randomUUID().toString(), "accessToken3", "refreshToken3")
        val authenticationCredentials4 = AuthenticationCredentials(UUID.randomUUID().toString(), "accessToken4", "refreshToken4")
        val authenticationCredentials5 = AuthenticationCredentials(UUID.randomUUID().toString(), "accessToken5", "refreshToken5")

        tokenRepository.save(authenticationCredentials1)
        tokenRepository.save(authenticationCredentials2)
        tokenRepository.save(authenticationCredentials3)
        tokenRepository.save(authenticationCredentials4)
        tokenRepository.save(authenticationCredentials5)

        // when
        tokenRepository.deleteAll()

        // then
        val result1 = tokenRepository.findByJti(authenticationCredentials1.jti)
        val result2 = tokenRepository.findByJti(authenticationCredentials2.jti)
        val result3 = tokenRepository.findByJti(authenticationCredentials3.jti)
        val result4 = tokenRepository.findByJti(authenticationCredentials4.jti)
        val result5 = tokenRepository.findByJti(authenticationCredentials5.jti)

        assertThat(result1).isNull()
        assertThat(result2).isNull()
        assertThat(result3).isNull()
        assertThat(result4).isNull()
        assertThat(result5).isNull()
    }

}