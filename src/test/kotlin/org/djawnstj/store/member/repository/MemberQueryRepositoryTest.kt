package org.djawnstj.store.member.repository

import org.assertj.core.api.Assertions.assertThat
import org.djawnstj.store.IntegrationTestSupport
import org.djawnstj.store.member.entity.Member
import org.junit.jupiter.api.*
import org.springframework.beans.factory.annotation.Autowired

@TestMethodOrder(
    MethodOrderer.OrderAnnotation::class)
class MemberQueryRepositoryTest: IntegrationTestSupport() {

    @Autowired
    private lateinit var memberQueryRepository: MemberQueryRepository

    @Autowired
    private lateinit var memberJpaRepository: MemberJpaRepository

    @AfterEach
    fun tearDown() {
        memberJpaRepository.deleteAll()
    }

    @Test
    @Order(1)
    fun `휴대폰 번호를 통해 회원을 조회한다`() {
        // given
        val phoneNumber = "010-0000-0000"
        val member = memberJpaRepository.save(Member(phoneNumber, "password", "name"))

        // when
        val result = memberQueryRepository.findByPhoneNumber(phoneNumber)

        // then
        assertThat(result)
            .extracting("phoneNumber", "loginPassword", "name", "role")
            .contains(member.phoneNumber, member.loginPassword, member.name, member.role)
    }

    @Test
    @Order(2)
    fun `휴대폰 번호를 통해 회원을 조회할 때, 일치하는 휴대폰 번호를 가진 회원 없다면 null 을 반환한다`() {
        // when
        val result = memberQueryRepository.findByPhoneNumber("010-0000-0000")

        // then
        assertThat(result).isNull()
    }
    
}