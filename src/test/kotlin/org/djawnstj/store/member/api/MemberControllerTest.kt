package org.djawnstj.store.member.api

import io.mockk.justRun
import org.djawnstj.store.ControllerTestSupport
import org.djawnstj.store.member.dto.signup.SignUpRequest
import org.djawnstj.store.member.entity.MemberRole
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers

class MemberControllerTest: ControllerTestSupport() {

    @Test
    fun `휴대폰 번호, 비밀번호, 이름, 회원구분 을 받아 회원가입을 진행한다`() {
        // given
        val request = SignUpRequest("010-0000-0000", "password", "홍길동", MemberRole.OWNER)

        justRun { memberService.signUp(request) }

        // when then
        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/v1/members")
                .content(objectMapper.writeValueAsBytes(request))
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun `회원가입을 할때, 올바른 휴대폰 번호 형식을 입력해야 한다`() {
        // given
        val request1 = SignUpRequest("", "password", "홍길동", MemberRole.OWNER)
        val request2 = SignUpRequest(null, "password", "홍길동", MemberRole.OWNER)
        val request3 = SignUpRequest("0-0000-0000", "password", "홍길동", MemberRole.OWNER)
        val request4 = SignUpRequest("0010-0000-0000", "password", "홍길동", MemberRole.OWNER)
        val request5 = SignUpRequest("010-00-0000", "password", "홍길동", MemberRole.OWNER)
        val request6 = SignUpRequest("010-00000-0000", "password", "홍길동", MemberRole.OWNER)
        val request7 = SignUpRequest("010-0000-00000", "password", "홍길동", MemberRole.OWNER)
        val request8 = SignUpRequest("010-0000-000", "password", "홍길동", MemberRole.OWNER)

        // when then
        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/v1/members")
                .content(objectMapper.writeValueAsBytes(request1))
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(MockMvcResultHandlers.print())
            .isInvalidInputValueResponse("올바른 핸드폰 번호 양식이 아닙니다. ex) 010-0000-0000")
        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/v1/members")
                .content(objectMapper.writeValueAsBytes(request2))
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(MockMvcResultHandlers.print())
            .isInvalidInputValueResponse("올바른 핸드폰 번호 양식이 아닙니다. ex) 010-0000-0000")
        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/v1/members")
                .content(objectMapper.writeValueAsBytes(request3))
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(MockMvcResultHandlers.print())
            .isInvalidInputValueResponse("올바른 핸드폰 번호 양식이 아닙니다. ex) 010-0000-0000")
        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/v1/members")
                .content(objectMapper.writeValueAsBytes(request4))
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(MockMvcResultHandlers.print())
            .isInvalidInputValueResponse("올바른 핸드폰 번호 양식이 아닙니다. ex) 010-0000-0000")
        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/v1/members")
                .content(objectMapper.writeValueAsBytes(request5))
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(MockMvcResultHandlers.print())
            .isInvalidInputValueResponse("올바른 핸드폰 번호 양식이 아닙니다. ex) 010-0000-0000")
        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/v1/members")
                .content(objectMapper.writeValueAsBytes(request6))
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(MockMvcResultHandlers.print())
            .isInvalidInputValueResponse("올바른 핸드폰 번호 양식이 아닙니다. ex) 010-0000-0000")
        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/v1/members")
                .content(objectMapper.writeValueAsBytes(request7))
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(MockMvcResultHandlers.print())
            .isInvalidInputValueResponse("올바른 핸드폰 번호 양식이 아닙니다. ex) 010-0000-0000")
        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/v1/members")
                .content(objectMapper.writeValueAsBytes(request8))
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(MockMvcResultHandlers.print())
            .isInvalidInputValueResponse("올바른 핸드폰 번호 양식이 아닙니다. ex) 010-0000-0000")
    }

    @Test
    fun `회원가입을 할때, 비밀번호 입력은 필수입니다`() {
        // given
        val request1 = SignUpRequest("010-0000-0000", "", "홍길동", MemberRole.OWNER)
        val request2 = SignUpRequest("010-0000-0000", null, "홍길동", MemberRole.OWNER)

        // when then
        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/v1/members")
                .content(objectMapper.writeValueAsBytes(request1))
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(MockMvcResultHandlers.print())
            .isInvalidInputValueResponse("비밀번호는 필수로 입력하셔야 합니다.")
        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/v1/members")
                .content(objectMapper.writeValueAsBytes(request2))
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(MockMvcResultHandlers.print())
            .isInvalidInputValueResponse("비밀번호는 필수로 입력하셔야 합니다.")
    }

    @Test
    fun `회원가입을 할때, 이름 입력은 필수입니다`() {
        // given
        val request1 = SignUpRequest("010-0000-0000", "password", "", MemberRole.OWNER)
        val request2 = SignUpRequest("010-0000-0000", "password", null, MemberRole.OWNER)

        // when then
        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/v1/members")
                .content(objectMapper.writeValueAsBytes(request1))
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(MockMvcResultHandlers.print())
            .isInvalidInputValueResponse("이름은 필수로 입력하셔야 합니다.")
        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/v1/members")
                .content(objectMapper.writeValueAsBytes(request2))
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(MockMvcResultHandlers.print())
            .isInvalidInputValueResponse("이름은 필수로 입력하셔야 합니다.")
    }

    @Test
    fun `회원가입을 할때, 올바른 회원 구분을 입력해야 합니다`() {
        // given
        val request = SignUpRequest("010-0000-0000", "password", " name", null)

        // when then
        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/v1/members")
                .content(objectMapper.writeValueAsBytes(request))
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(MockMvcResultHandlers.print())
            .isInvalidInputValueResponse("올바른 회원 구분이 아닙니다.")
    }

}