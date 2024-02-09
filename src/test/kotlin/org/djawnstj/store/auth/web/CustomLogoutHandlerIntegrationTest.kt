package org.djawnstj.store.auth.web

import org.assertj.core.api.Assertions.assertThat
import org.djawnstj.store.IntegrationTestSupport
import org.djawnstj.store.auth.repository.TokenRepository
import org.djawnstj.store.auth.service.JwtService
import org.djawnstj.store.member.entity.Member
import org.djawnstj.store.member.repository.MemberJpaRepository
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.web.client.postForEntity
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders

class CustomLogoutHandlerIntegrationTest: IntegrationTestSupport() {

    @Autowired
    private lateinit var memberJpaRepository: MemberJpaRepository
    @Autowired
    private lateinit var jwtService: JwtService
    @Autowired
    private lateinit var tokenRepository: TokenRepository

    @AfterEach
    fun tearDown() {
        memberJpaRepository.deleteAll()
        tokenRepository.deleteAll()
    }

    @Test
    fun `로그아웃 요청을 받아 로그아웃을 수행한다`() {
        // given
        val member = memberJpaRepository.save(Member("010-0000-0000", "password", "name"))
        val authenticationCredentials = jwtService.generateAuthenticationCredentials(member)
        val jti = authenticationCredentials.jti

        tokenRepository.save(authenticationCredentials)

        val entity = HttpHeaders().apply {
            set("Authorization", "Bearer ${authenticationCredentials.accessToken}")
        }.let {
            HttpEntity<String>("", it)
        }

        // when
        restTemplate.postForEntity<String>("http://localhost:$port/api/v1/auth/logout", entity)

        // then
        val result = tokenRepository.findByJti(jti)
        assertThat(result).isNull()
    }

}