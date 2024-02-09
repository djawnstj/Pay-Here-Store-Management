package org.djawnstj.store.member.repository

import com.linecorp.kotlinjdsl.support.spring.data.jpa.repository.KotlinJdslJpqlExecutor
import org.djawnstj.store.member.entity.Member
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface MemberJpaRepository: JpaRepository<Member, Long>, KotlinJdslJpqlExecutor {
}