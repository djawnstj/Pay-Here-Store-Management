package org.djawnstj.store.auth.dto.refresh

import jakarta.validation.constraints.NotBlank

data class TokenRefreshRequest(
    @field:NotBlank(message = "토큰 값은 필수 입니다.")
    val refreshToken: String?,
)