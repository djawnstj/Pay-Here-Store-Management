package org.djawnstj.store.auth.repository

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.djawnstj.store.auth.config.JwtProperties
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.core.ValueOperations
import org.springframework.stereotype.Repository
import java.util.concurrent.TimeUnit

@Repository
class TokenRedisRepository(
    private val redisTemplate: RedisTemplate<String, String>,
    private val jwtProperties: JwtProperties,
): TokenRepository {
    private val objectMapper: ObjectMapper = jacksonObjectMapper()
    private val valueOperations: ValueOperations<String, String>
        get() = this.redisTemplate.opsForValue()

    override fun save(authenticationCredentials: Token): Token {
        valueOperations[authenticationCredentials.jti] = objectMapper.writeValueAsString(authenticationCredentials)

        valueOperations.getAndExpire(authenticationCredentials.jti, jwtProperties.refreshTokenExpiration, TimeUnit.MILLISECONDS)
        
        return authenticationCredentials
    }

    override fun findByJti(jti: String): Token? =
        valueOperations[jti]?.let { objectMapper.readValue(it, Token::class.java) } ?: run { null }

    override fun deleteByJti(jti: String) {
        redisTemplate.delete(jti)
    }

    override fun deleteAll() {
        redisTemplate.delete(redisTemplate.keys("*"))
    }
}