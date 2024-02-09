package org.djawnstj.store.auth.dto.refresh

data class TokenRefreshResponse(
    val accessToken: String,
    val refreshToken: String,
)
