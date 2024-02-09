package org.djawnstj.store.auth.service

import org.assertj.core.api.Assertions.assertThat
import org.djawnstj.store.IntegrationTestSupport
import org.djawnstj.store.member.entity.Member
import org.djawnstj.store.member.repository.MemberJpaRepository
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.util.*

class JjwtTokenProviderTest: IntegrationTestSupport() {

    @Autowired
    private lateinit var tokenProvider: TokenProvider

    @Autowired
    private lateinit var memberJpaRepository: MemberJpaRepository

    @AfterEach
    fun tearDown() {
        memberJpaRepository.deleteAll()
    }

    @Test
    fun `토큰에서 username 을 추출한다`() {
        // given
        val member = memberJpaRepository.save(Member("010-0000-0000", "loginPassword", "name"))
        val jti = UUID.randomUUID().toString()
        val token = tokenProvider.buildToken(member, jti, 1000L)

        // when
        val result = tokenProvider.extractUsername(token)

        // then
        assertThat(result).isEqualTo(member.phoneNumber)
    }

    @Test
    fun `토큰에서 jti 을 추출한다`() {
        // given
        val member = memberJpaRepository.save(Member("010-0000-0000", "loginPassword", "name"))
        val jti = UUID.randomUUID().toString()
        val token = tokenProvider.buildToken(member, jti, 1000L)

        // when
        val result = tokenProvider.extractJti(token)

        // then
        assertThat(result).isEqualTo(jti)
    }

    @Test
    fun `토큰에서 만료시간을 추출한다`() {
        // given
        val member = memberJpaRepository.save(Member("010-0000-0000", "loginPassword", "name"))
        val jti = UUID.randomUUID().toString()
        val token = tokenProvider.buildToken(member, jti, 1000L)

        // when
        val result = tokenProvider.extractExpiration(token)

        // then
        assertThat(result).isAfter(Date())
    }

    @Test
    fun `토큰을 생성한다`() {
        // given
        val member = memberJpaRepository.save(Member("010-0000-0000", "loginPassword", "name"))
        val jti = UUID.randomUUID().toString()

        // when
        val result = tokenProvider.buildToken(member, jti, 1000L)

        // then
        assertThat(result).isNotBlank()
    }

}