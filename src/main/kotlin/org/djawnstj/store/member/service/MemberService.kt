package org.djawnstj.store.member.service

import org.djawnstj.store.common.exception.ErrorCode
import org.djawnstj.store.common.exception.GlobalException
import org.djawnstj.store.member.dto.signup.SignUpRequest
import org.djawnstj.store.member.entity.Member
import org.djawnstj.store.member.repository.MemberJpaRepository
import org.djawnstj.store.member.repository.MemberQueryRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class MemberService(
    private val memberJpaRepository: MemberJpaRepository,
    private val memberQueryRepository: MemberQueryRepository,
    private val passwordEncoder: PasswordEncoder,
) {

    @Transactional
    fun signUp(request: SignUpRequest) {
        validateDuplicatedPhoneNumber(request.phoneNumber)

        val member = request.let {
            Member(it.phoneNumber, passwordEncoder.encode(it.loginPassword), it.name!!, it.role!!)
        }

        memberJpaRepository.save(member)
    }

    private fun validateDuplicatedPhoneNumber(phoneNumber: String?) {
        val foundMember = memberQueryRepository.findByPhoneNumber(phoneNumber)
        if (foundMember != null) throw GlobalException(ErrorCode.DUPLICATED_REGISTER_PHONE_NUMBER)
    }

}