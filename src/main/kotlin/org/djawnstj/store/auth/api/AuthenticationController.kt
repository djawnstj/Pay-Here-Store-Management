package org.djawnstj.store.auth.api

import jakarta.validation.Valid
import org.djawnstj.store.auth.dto.signin.LogInRequest
import org.djawnstj.store.auth.dto.signin.LogInResponse
import org.djawnstj.store.auth.service.AuthenticationService
import org.djawnstj.store.common.api.Response
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class AuthenticationController(
    private val authenticationService: AuthenticationService
) {

    @PostMapping("/api/v1/auth/log-in")
    fun logIn(@Valid @RequestBody request: LogInRequest): Response<LogInResponse> =
        Response.success(authenticationService.logIn(request))

}