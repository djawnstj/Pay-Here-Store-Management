package org.djawnstj.store.auth.repository

import org.springframework.transaction.annotation.Transactional

//@Repository
@Transactional(readOnly = true)
class TokenQueryRepository(
    private val tokenJpaRepository: TokenJpaRepository
): TokenRepository {

    @Transactional
    override fun save(authenticationCredentials: Token) = tokenJpaRepository.save(authenticationCredentials)

    override fun findByJti(jti: String): Token? = tokenJpaRepository.findAll {
        select(
            entity(Token::class)
        ).from(
            entity(Token::class)
        ).where(
            path(Token::jti).equal(jti)
        )
    }.firstOrNull()

    @Transactional
    override fun deleteByJti(jti: String) {
        tokenJpaRepository.delete {
            deleteFrom(
                entity(Token::class)
            ).where(
                path(Token::jti).equal(jti)
            )
        }
    }

    @Transactional
    override fun deleteAll() {
        tokenJpaRepository.deleteAll()
    }
}