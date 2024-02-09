package org.djawnstj.store.auth.dto.signin

data class SignInResponse(
    val accessToken: String,
    val refreshToken: String
)