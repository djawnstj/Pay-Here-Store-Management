package org.djawnstj.store.auth.api

import io.mockk.every
import io.mockk.mockk
import org.djawnstj.store.ControllerTestSupport
import org.djawnstj.store.auth.dto.refresh.TokenRefreshRequest
import org.djawnstj.store.auth.dto.signin.LogInRequest
import org.djawnstj.store.auth.dto.signin.LogInResponse
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers

class AuthenticationControllerTest: ControllerTestSupport() {

    @Test
    fun `휴대폰 번호와 비밀번호를 입력받아 로그인을 진행한다`() {
        // given
        val request = LogInRequest("010-0000-0000", "password")

        every { authenticationService.logIn(request) } returns LogInResponse("accessToken", "refreshToken")

        // when then
        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/v1/auth/log-in")
                .content(objectMapper.writeValueAsBytes(request))
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun `로그인을 할때, 올바른 휴대폰 번호 형식을 입력해야 한다`() {
        // given
        val request1 = LogInRequest("", "password")
        val request2 = LogInRequest(null, "password")
        val request3 = LogInRequest("0-0000-0000", "password")
        val request4 = LogInRequest("0010-0000-0000", "password")
        val request5 = LogInRequest("010-00-0000", "password")
        val request6 = LogInRequest("010-00000-0000", "password")
        val request7 = LogInRequest("010-0000-00000", "password")
        val request8 = LogInRequest("010-0000-00", "password")

        // when then
        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/v1/auth/log-in")
                .content(objectMapper.writeValueAsBytes(request1))
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(MockMvcResultHandlers.print())
            .isInvalidInputValueResponse("올바른 핸드폰 번호 양식이 아닙니다. ex) 010-0000-0000")
        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/v1/auth/log-in")
                .content(objectMapper.writeValueAsBytes(request2))
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(MockMvcResultHandlers.print())
            .isInvalidInputValueResponse("올바른 핸드폰 번호 양식이 아닙니다. ex) 010-0000-0000")
        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/v1/auth/log-in")
                .content(objectMapper.writeValueAsBytes(request3))
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(MockMvcResultHandlers.print())
            .isInvalidInputValueResponse("올바른 핸드폰 번호 양식이 아닙니다. ex) 010-0000-0000")
        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/v1/auth/log-in")
                .content(objectMapper.writeValueAsBytes(request4))
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(MockMvcResultHandlers.print())
            .isInvalidInputValueResponse("올바른 핸드폰 번호 양식이 아닙니다. ex) 010-0000-0000")
        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/v1/auth/log-in")
                .content(objectMapper.writeValueAsBytes(request5))
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(MockMvcResultHandlers.print())
            .isInvalidInputValueResponse("올바른 핸드폰 번호 양식이 아닙니다. ex) 010-0000-0000")
        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/v1/auth/log-in")
                .content(objectMapper.writeValueAsBytes(request6))
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(MockMvcResultHandlers.print())
            .isInvalidInputValueResponse("올바른 핸드폰 번호 양식이 아닙니다. ex) 010-0000-0000")
        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/v1/auth/log-in")
                .content(objectMapper.writeValueAsBytes(request7))
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(MockMvcResultHandlers.print())
            .isInvalidInputValueResponse("올바른 핸드폰 번호 양식이 아닙니다. ex) 010-0000-0000")
        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/v1/auth/log-in")
                .content(objectMapper.writeValueAsBytes(request8))
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(MockMvcResultHandlers.print())
            .isInvalidInputValueResponse("올바른 핸드폰 번호 양식이 아닙니다. ex) 010-0000-0000")
    }

    @Test
    fun `로그인을 할때, 비밀번호 입력은 필수입니다`() {
        // given
        val request1 = LogInRequest("010-0000-0000", "")
        val request2 = LogInRequest("010-0000-0000", null)

        // when then
        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/v1/auth/log-in")
                .content(objectMapper.writeValueAsBytes(request1))
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(MockMvcResultHandlers.print())
            .isInvalidInputValueResponse("비밀번호는 필수로 입력하셔야 합니다.")
        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/v1/auth/log-in")
                .content(objectMapper.writeValueAsBytes(request2))
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(MockMvcResultHandlers.print())
            .isInvalidInputValueResponse("비밀번호는 필수로 입력하셔야 합니다.")
    }

    @Test
    fun `토큰 재발급`() {
        // given
        val request1 = TokenRefreshRequest("refreshToken")

        every { authenticationService.refreshToken(request1) } returns mockk(relaxed = true)

        // when then
        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/v1/auth/refresh")
                .content(objectMapper.writeValueAsBytes(request1))
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(MockMvcResultHandlers.print())
            .isOkBaseResponse()
    }

    @Test
    fun `토큰을 재발급 할때, 토큰값은 필수입니다`() {
        // given
        val request1 = TokenRefreshRequest("")
        val request2 = TokenRefreshRequest(null)

        // when then
        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/v1/auth/refresh")
                .content(objectMapper.writeValueAsBytes(request1))
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(MockMvcResultHandlers.print())
            .isInvalidInputValueResponse("토큰 값은 필수 입니다.")
        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/v1/auth/refresh")
                .content(objectMapper.writeValueAsBytes(request2))
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(MockMvcResultHandlers.print())
            .isInvalidInputValueResponse("토큰 값은 필수 입니다.")
    }

}