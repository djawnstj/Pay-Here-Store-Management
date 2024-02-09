package org.djawnstj.store.auth.service

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.djawnstj.store.auth.config.JwtProperties
import org.djawnstj.store.common.exception.ErrorCode
import org.djawnstj.store.common.exception.GlobalException
import org.djawnstj.store.common.provider.Base64Provider
import org.djawnstj.store.common.provider.TimeProvider
import org.djawnstj.store.common.provider.UUIDProvider
import org.djawnstj.store.member.entity.Member
import org.junit.jupiter.api.Test
import org.springframework.security.core.userdetails.UserDetails
import java.util.*

class JwtServiceTest {

    private val tokenProvider: TokenProvider = mockk()
    private val jwtProperties: JwtProperties = mockk(relaxed = true)
    private val objectMapper: ObjectMapper = mockk()
    private val base64Provider: Base64Provider = mockk()
    private val uuidProvider: UUIDProvider = mockk()
    private val timeProvider: TimeProvider = mockk()

    private val jwtService: JwtService = JwtService(
        tokenProvider,
        jwtProperties,
        objectMapper,
        base64Provider,
        uuidProvider,
        timeProvider,)

    @Test
    fun `토큰을 받아서 username 을 반환한다`() {
        // given
        val token = "token"

        every { tokenProvider.extractUsername(token) } returns "username"

        // when
        val username = jwtService.getUsername(token)

        // then
        assertThat(username).isNotBlank()
    }

    @Test
    fun `토큰의 username 반환할 때, 토큰의 username 이 null 이거나 공백이면 예외를 반환한다`() {
        // given
        val token1 = "token1"
        val token2 = "token2"

        every { tokenProvider.extractUsername(token1) } returns null
        every { tokenProvider.extractUsername(token2) } returns ""

        // when then
        assertThatThrownBy { jwtService.getUsername(token1) }
            .isInstanceOf(GlobalException::class.java)
            .hasMessage(ErrorCode.EMPTY_CLAIM.message)
        assertThatThrownBy { jwtService.getUsername(token2) }
            .isInstanceOf(GlobalException::class.java)
            .hasMessage(ErrorCode.EMPTY_CLAIM.message)
    }

    @Test
    fun `토큰을 받아서 jti 을 반환한다`() {
        // given
        val token = "token"

        every { tokenProvider.extractJti(token) } returns "jti"

        // when
        val jti = jwtService.getJti(token)

        // then
        assertThat(jti).isEqualTo("jti")
    }

    @Test
    fun `토큰의 jti 반환할 때, 토큰의 jti 이 null 이거나 공백이면 예외를 반환한다`() {
        // given
        val token1 = "token1"
        val token2 = "token2"

        every { tokenProvider.extractJti(token1) } returns null
        every { tokenProvider.extractJti(token2) } returns ""

        // when then
        assertThatThrownBy { jwtService.getJti(token1) }
            .isInstanceOf(GlobalException::class.java)
            .hasMessage(ErrorCode.EMPTY_CLAIM.message)
        assertThatThrownBy { jwtService.getJti(token2) }
            .isInstanceOf(GlobalException::class.java)
            .hasMessage(ErrorCode.EMPTY_CLAIM.message)
    }

    @Test
    fun `userDetail 과 jti 를 받아 accessToken 을 생성한다`() {
        // given
        val userDetails: UserDetails = mockk()
        val jti = "key"

        every { tokenProvider.buildToken(userDetails, jti, any<Long>()) } returns "token"

        // when
        val accessToken = jwtService.generateAccessToken(userDetails, jti)

        // then
        assertThat(accessToken).isNotBlank()
    }

    @Test
    fun `userDetail 과 jti 를 받아 refreshToken 을 생성한다`() {
        // given
        val userDetails: UserDetails = mockk()
        val jti = "key"

        every { tokenProvider.buildToken(userDetails, jti, any<Long>()) } returns "token"

        // when
        val refreshToken = jwtService.generateRefreshToken(userDetails, jti)

        // then
        assertThat(refreshToken).isNotBlank()
    }

    @Test
    fun `member 객체를 받아 토큰 정보를 담은 AuthenticationCredentials 를 반환한다`() {
        // given
        val member: Member = mockk()
        val uuidStr = "uuid"

        every { uuidProvider.generateUuidString() } returns uuidStr
        every { tokenProvider.buildToken(member, uuidStr, any<Long>()) } returns "token"
        every { member.id } returns 1L

        // when
        val authenticationCredentials = jwtService.generateAuthenticationCredentials(member)

        // then
        assertThat(authenticationCredentials).isNotNull
    }

    @Test
    fun `토큰이 유효한지 검증한다`() {
        // given
        val token = "token"
        val userDetails: UserDetails = mockk()
        val username = "username"
        val expiration: Date = mockk()

        every { tokenProvider.extractUsername(token) } returns username
        every { userDetails.username } returns username
        every { tokenProvider.extractExpiration(token) } returns expiration
        every { expiration.after(any<Date>()) } returns true

        // when
        val isTokenValid = jwtService.isTokenValid(token, userDetails)

        // then
        assertThat(isTokenValid).isTrue()
    }

    @Test
    fun `토큰 유효성 검증에서 토큰의 username 과 userDetails 의 username 이 다르면 false 를 반환한다`() {
        // given
        val token = "token"
        val userDetails: UserDetails = mockk()
        val username1 = "username1"
        val username2 = "username2"

        every { tokenProvider.extractUsername(token) } returns username1
        every { userDetails.username } returns username2

        // when
        val isTokenValid = jwtService.isTokenValid(token, userDetails)

        // then
        assertThat(isTokenValid).isFalse()
    }

    @Test
    fun `토큰 유효성 검증에서 토큰의 만료시간이 지났으면 true 를 반환한다`() {
        // given
        val token = "token"
        val userDetails: UserDetails = mockk()
        val username = "username"
        val expiration: Date = mockk()

        every { tokenProvider.extractUsername(token) } returns username
        every { userDetails.username } returns username
        every { tokenProvider.extractExpiration(token) } returns expiration
        every { expiration.after(any<Date>()) } returns false

        // when
        val isTokenValid = jwtService.isTokenValid(token, userDetails)

        // then
        assertThat(isTokenValid).isFalse()
    }

    @Test
    fun `토큰 유효성 검증에서 토큰에 만료시간이 null 이면 false 를 반환한다`() {
        // given
        val token = "token"
        val userDetails: UserDetails = mockk()
        val username = "username"

        every { tokenProvider.extractUsername(token) } returns username
        every { userDetails.username } returns username
        every { tokenProvider.extractExpiration(token) } returns null

        // when
        val isTokenValid = jwtService.isTokenValid(token, userDetails)

        // then
        assertThat(isTokenValid).isFalse()
    }

    @Test
    fun `토큰 문자열을 이용해 만료시간이 지나지 않았으면 false 를 반환한다`() {
        // given
        val token = "token.token.token"
        val bytes = ByteArray(1) { 0x01 }
        val tokenPart = "token"
        val readObject = mutableMapOf("exp" to "1000")
        val currentMillis = 900L

        every { base64Provider.decode(tokenPart) } returns bytes
        every { objectMapper.readValue(any<String>(), any<TypeReference<MutableMap<String, String>>>()) } returns readObject
        every { timeProvider.getCurrentTimeMillis() } returns currentMillis

        // when
        val isExpired = jwtService.checkTokenExpiredByTokenString(token)

        // then
        assertThat(isExpired).isFalse()
    }

    @Test
    fun `토큰 문자열을 이용해 만료시간이 지났으면 true 를 반환한다`() {
        // given
        val token = "token.token.token"
        val bytes = ByteArray(1) { 0x01 }
        val tokenPart = "token"
        val readObject = mutableMapOf("exp" to "1000")
        val currentMillis = 900_000_000L

        every { base64Provider.decode(tokenPart) } returns bytes
        every { objectMapper.readValue(any<String>(), any<TypeReference<MutableMap<String, String>>>()) } returns readObject
        every { timeProvider.getCurrentTimeMillis() } returns currentMillis

        // when
        val isExpired = jwtService.checkTokenExpiredByTokenString(token)

        // then
        assertThat(isExpired).isTrue()
    }

    @Test
    fun `토큰 문자열을 이용해 만료시간을 체크할 때 토큰에 만료시간이 없다면 예외를 반환한다`() {
        // given
        val token = "token.token.token"
        val bytes = ByteArray(1) { 0x01 }
        val tokenPart = "token"
        val readObject = mutableMapOf("" to "")

        every { base64Provider.decode(tokenPart) } returns bytes
        every { objectMapper.readValue(any<String>(), any<TypeReference<MutableMap<String, String>>>()) } returns readObject

        // when then
        assertThatThrownBy { jwtService.checkTokenExpiredByTokenString(token) }
            .isInstanceOf(GlobalException::class.java)
            .hasMessage(ErrorCode.INVALID_ACCESS_TOKEN.message)
    }

}