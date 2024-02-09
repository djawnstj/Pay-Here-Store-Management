package org.djawnstj.store.member.service

import org.assertj.core.api.Assertions.assertThat
import org.djawnstj.store.IntegrationTestSupport
import org.djawnstj.store.member.dto.signup.SignUpRequest
import org.djawnstj.store.member.entity.MemberRole
import org.djawnstj.store.member.repository.MemberJpaRepository
import org.djawnstj.store.member.repository.MemberQueryRepository
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.password.PasswordEncoder

class MemberServiceIntegrationTest: IntegrationTestSupport() {

    @Autowired
    private lateinit var memberService: MemberService

    @Autowired
    private lateinit var memberQueryRepository: MemberQueryRepository
    @Autowired
    private lateinit var memberJpaRepository: MemberJpaRepository
    @Autowired
    private lateinit var passwordEncoder: PasswordEncoder

    @AfterEach
    fun tearDown() {
        memberJpaRepository.deleteAll()
    }

    @Test
    fun `회원 가입 요청을 받아 회원가입을 진행한다`() {
        // given
        val request = SignUpRequest("010-0000-0000", "password", "name", MemberRole.OWNER)

        // when
        memberService.signUp(request)

        // then
        val result = memberQueryRepository.findByPhoneNumber(request.phoneNumber)
        assertThat(result).isNotNull
        assertThat(result!!)
            .extracting("phoneNumber", "name", "role")
            .contains(request.phoneNumber, request.name, request.role)
        assertThat(passwordEncoder.matches(request.loginPassword, result.password))
    }

}