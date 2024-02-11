package org.djawnstj.store

import org.djawnstj.store.member.entity.MemberRole
import org.springframework.security.test.context.support.TestExecutionEvent
import org.springframework.security.test.context.support.WithSecurityContext

@Target(AnnotationTarget.FUNCTION, AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@WithSecurityContext(factory = WithCustomMockUserSecurityContextFactory::class)
annotation class WithCustomMockUser(
    val phoneNumber: String = "loginId",
    val loginPassword: String = "password",
    val name: String = "name",
    val role: MemberRole = MemberRole.OWNER,
    val setupBefore: TestExecutionEvent = TestExecutionEvent.TEST_EXECUTION
)
