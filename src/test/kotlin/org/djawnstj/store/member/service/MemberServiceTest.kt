package org.djawnstj.store.member.service

import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.djawnstj.store.common.exception.ErrorCode
import org.djawnstj.store.common.exception.GlobalException
import org.djawnstj.store.member.dto.signup.SignUpRequest
import org.djawnstj.store.member.entity.Member
import org.djawnstj.store.member.entity.MemberRole
import org.djawnstj.store.member.repository.MemberJpaRepository
import org.djawnstj.store.member.repository.MemberQueryRepository
import org.junit.jupiter.api.Test
import org.springframework.security.crypto.password.PasswordEncoder

class MemberServiceTest {

    private val memberJpaRepository: MemberJpaRepository = mockk()
    private val memberQueryRepository: MemberQueryRepository = mockk()
    private val passwordEncoder: PasswordEncoder = mockk()

    private val memberService = MemberService(memberJpaRepository, memberQueryRepository, passwordEncoder)

    @Test
    fun `휴대폰 번호, 비밀번호, 이름, 회원 구분 을 받아 회원가입을 진행한다`() {
        // given
        val request = SignUpRequest("010-0000-0000", "password", "name", MemberRole.OWNER)

        every { memberQueryRepository.findByPhoneNumber(request.phoneNumber) } returns null
        every { passwordEncoder.encode(request.loginPassword) } returns request.loginPassword
        every { memberJpaRepository.save(any<Member>()) } answers { it.invocation.args[0] as Member }

        // when then
        memberService.signUp(request)
    }

    @Test
    fun `회원가입을 요청한 휴대폰 번호로 이미 가입된 회원이 있다면 예외를 반환한다`() {
        // given
        val request = SignUpRequest("010-0000-0000", "password", "name", MemberRole.OWNER)

        every { memberQueryRepository.findByPhoneNumber(request.phoneNumber) } returns mockk()

        // when then
        assertThatThrownBy { memberService.signUp(request) }
            .isInstanceOf(GlobalException::class.java)
            .hasMessage(ErrorCode.DUPLICATED_REGISTER_PHONE_NUMBER.message)
    }

}