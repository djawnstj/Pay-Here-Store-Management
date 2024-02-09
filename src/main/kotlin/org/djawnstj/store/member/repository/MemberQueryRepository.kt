package org.djawnstj.store.member.repository

import org.djawnstj.store.member.entity.Member
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@Transactional(readOnly = true)
class MemberQueryRepository(
    private val memberJpaRepository: MemberJpaRepository
) {

    fun findByPhoneNumber(phoneNumber: String?): Member? = memberJpaRepository.findAll {
        select(
            entity(Member::class)
        ).from(
            entity(Member::class)
        ).where(
            path(Member::deletedAt).isNull()
                .and(
                    path(Member::phoneNumber).equal(phoneNumber)
                )
        )
    }.firstOrNull()

}