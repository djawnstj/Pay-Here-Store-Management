package org.djawnstj.store.auth.repository

import org.djawnstj.store.auth.entity.AuthenticationCredentials

interface TokenRepository {

    fun save(authenticationCredentials: AuthenticationCredentials): AuthenticationCredentials

    fun findByJti(jti: String): AuthenticationCredentials?

    fun deleteByJti(jti: String)

    fun deleteAll()

}