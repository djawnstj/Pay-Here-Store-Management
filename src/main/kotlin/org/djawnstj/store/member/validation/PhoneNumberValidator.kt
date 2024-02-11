package org.djawnstj.store.member.validation

import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import java.util.regex.Pattern

class PhoneNumberValidator: ConstraintValidator<PhoneNumber, String> {

    private lateinit var regexp: String

    override fun isValid(value: String?, context: ConstraintValidatorContext): Boolean =
        if (value.isNullOrBlank()) false else Pattern.matches(regexp, value)

    override fun initialize(constraintAnnotation: PhoneNumber) {
        regexp = constraintAnnotation.regexp
    }
}