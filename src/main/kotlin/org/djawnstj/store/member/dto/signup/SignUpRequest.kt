package org.djawnstj.store.member.dto.signup

import jakarta.validation.constraints.NotBlank
import org.djawnstj.store.common.validation.ValidEnum
import org.djawnstj.store.member.entity.MemberRole
import org.djawnstj.store.member.validation.PhoneNumber

data class SignUpRequest(
    @field:PhoneNumber
    val phoneNumber: String?,
    @field:NotBlank(message = "비밀번호는 필수로 입력하셔야 합니다.")
    val loginPassword: String?,
    @field:NotBlank(message = "이름은 필수로 입력하셔야 합니다.")
    val name: String?,
    @field:ValidEnum(enumClass = MemberRole::class, message = "올바른 회원 구분이 아닙니다.")
    val role: MemberRole?,
)