package org.djawnstj.store.member.validation

import jakarta.validation.Constraint
import jakarta.validation.Payload
import kotlin.reflect.KClass

@Constraint(validatedBy = [PhoneNumberValidator::class])
@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class PhoneNumber(
    val message: String = "올바른 핸드폰 번호 양식이 아닙니다. ex) 010-0000-0000",
    val regexp: String = "^\\d{2,3}-\\d{3,4}-\\d{4}$",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)
