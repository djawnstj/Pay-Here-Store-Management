package org.djawnstj.store.auth.repository

import com.linecorp.kotlinjdsl.support.spring.data.jpa.repository.KotlinJdslJpqlExecutor
import org.djawnstj.store.auth.entity.AuthenticationCredentials
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TokenJpaRepository: JpaRepository<AuthenticationCredentials, Long>, KotlinJdslJpqlExecutor {
}