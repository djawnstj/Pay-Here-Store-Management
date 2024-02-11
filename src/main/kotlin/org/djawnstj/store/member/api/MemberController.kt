package org.djawnstj.store.member.api

import jakarta.validation.Valid
import org.djawnstj.store.member.dto.signup.SignUpRequest
import org.djawnstj.store.member.service.MemberService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
class MemberController(
    private val memberService: MemberService
) {

    @PostMapping("/api/v1/members")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun signUp(@Valid @RequestBody request: SignUpRequest) = memberService.signUp(request)

}