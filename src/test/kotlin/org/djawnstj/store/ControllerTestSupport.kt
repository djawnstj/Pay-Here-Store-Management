package org.djawnstj.store

import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import org.djawnstj.store.auth.api.AuthenticationController
import org.djawnstj.store.auth.config.SecurityConfig
import org.djawnstj.store.auth.repository.TokenRepository
import org.djawnstj.store.auth.service.AuthenticationService
import org.djawnstj.store.auth.service.JwtService
import org.djawnstj.store.auth.web.JwtAuthenticationFilter
import org.djawnstj.store.common.config.ServerConfig
import org.djawnstj.store.common.exception.ErrorCode
import org.djawnstj.store.member.api.MemberController
import org.djawnstj.store.member.service.MemberService
import org.djawnstj.store.product.api.ProductController
import org.djawnstj.store.product.service.ProductService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.h2.H2ConsoleProperties
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.FilterType
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.security.web.authentication.logout.LogoutHandler
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@WebMvcTest(controllers = [
    AuthenticationController::class,
    MemberController::class,
    ProductController::class,
],
    excludeFilters = [
        ComponentScan.Filter(
            type = FilterType.ASSIGNABLE_TYPE,
            classes = [
                ServerConfig::class,
            ]
        )
    ],
    includeFilters = [
        ComponentScan.Filter(
            type = FilterType.ASSIGNABLE_TYPE,
            classes = [
                JwtAuthenticationFilter::class,
                SecurityConfig::class,
            ]
        )
    ]
)
@MockkBean(JpaMetamodelMappingContext::class)
abstract class ControllerTestSupport {

    @Autowired
    protected lateinit var mockMvc: MockMvc

    @Autowired
    protected lateinit var objectMapper: ObjectMapper

    @MockkBean(relaxed = true)
    private lateinit var h2ConsoleProperties: H2ConsoleProperties
    @MockkBean
    private lateinit var jwtService: JwtService
    @MockkBean
    private lateinit var userDetailsService: UserDetailsService
    @MockkBean
    private lateinit var tokenRepository: TokenRepository
    @MockkBean
    private lateinit var authenticationProvider: AuthenticationProvider
    @MockkBean
    private lateinit var logoutHandler: LogoutHandler
    @MockkBean
    private lateinit var entryPoint: AuthenticationEntryPoint
    @MockkBean
    private lateinit var accessDeniedHandler: AccessDeniedHandler

    @MockkBean
    protected lateinit var authenticationService: AuthenticationService
    @MockkBean
    protected lateinit var memberService: MemberService
    @MockkBean
    protected lateinit var productService: ProductService

    protected fun ResultActions.baseResponse(status: HttpStatus): ResultActions =
        this.andExpect(MockMvcResultMatchers.jsonPath("$.meta.code").value(status.value()))
            .andExpect(MockMvcResultMatchers.jsonPath("$.meta.message").value(status.reasonPhrase))


    protected fun ResultActions.isOkBaseResponse(): ResultActions =
        this.andExpect(MockMvcResultMatchers.status().isOk)
            .baseResponse(HttpStatus.OK)

    protected fun ResultActions.isInvalidInputValueResponse(message: String): ResultActions =
        this.andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andExpect(MockMvcResultMatchers.jsonPath("$.meta.code").value(ErrorCode.INVALID_INPUT_VALUE.status.value()))
            .andExpect(MockMvcResultMatchers.jsonPath("$.meta.message").value(message))

}