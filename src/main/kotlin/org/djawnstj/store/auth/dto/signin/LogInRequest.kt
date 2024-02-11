package org.djawnstj.store.auth.dto.signin

import jakarta.validation.constraints.NotBlank
import org.djawnstj.store.member.validation.PhoneNumber

data class LogInRequest(
    @field:PhoneNumber
    var phoneNumber: String?,
    @field:NotBlank(message = "비밀번호는 필수로 입력하셔야 합니다.")
    var loginPassword: String?,
)
