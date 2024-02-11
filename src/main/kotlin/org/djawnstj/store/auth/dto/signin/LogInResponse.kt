package org.djawnstj.store.auth.dto.signin

data class LogInResponse(
    val accessToken: String,
    val refreshToken: String
)