package org.djawnstj.store.product.api

import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import org.djawnstj.store.ControllerTestSupport
import org.djawnstj.store.WithCustomMockUser
import org.djawnstj.store.common.exception.ErrorCode
import org.djawnstj.store.product.dto.categoryregistration.ProductCategoryRegistrationRequest
import org.djawnstj.store.product.dto.delete.ProductDeleteRequest
import org.djawnstj.store.product.dto.detail.ProductDetailRequest
import org.djawnstj.store.product.dto.list.ProductListRequest
import org.djawnstj.store.product.dto.registration.ProductRegistrationRequest
import org.djawnstj.store.product.dto.search.ProductSearchRequest
import org.djawnstj.store.product.dto.update.ProductUpdateRequest
import org.djawnstj.store.product.entity.CafeProductSize
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.math.BigDecimal
import java.time.LocalDate

class ProductControllerTest: ControllerTestSupport() {

    @Test
    @WithCustomMockUser
    fun `상품 카테고리를 등록한다`() {
        // given
        val request = ProductCategoryRegistrationRequest("커피")

        justRun { productService.registerProductCategory(request) }

        // when then
        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/v1/product-categories")
                .content(objectMapper.writeValueAsBytes(request))
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    @WithCustomMockUser
    fun `상품 카테고리를 등록할때 카테고리 이름은 필수로 입력해야 한다`() {
        // given
        val request1 = ProductCategoryRegistrationRequest("")
        val request2 = ProductCategoryRegistrationRequest(null)

        // when then
        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/v1/product-categories")
                .content(objectMapper.writeValueAsBytes(request1))
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(MockMvcResultHandlers.print())
            .isInvalidInputValueResponse("상품 카테고리 이름을 입력해 주세요.")
        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/v1/product-categories")
                .content(objectMapper.writeValueAsBytes(request2))
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(MockMvcResultHandlers.print())
            .isInvalidInputValueResponse("상품 카테고리 이름을 입력해 주세요.")
    }

    @Test
    @WithCustomMockUser
    fun `상품을 등록한다`() {
        // given
        val request = ProductRegistrationRequest(
            1L,
            BigDecimal("4000.00"),
            BigDecimal("1000.00"),
            "아메리카노",
            "고급 원두로 만든 아메리카노 입니다.",
            "barcode",
            LocalDate.of(2024, 2, 20),
            CafeProductSize.SMALL
        )

        justRun { productService.registerProduct(request) }

        // when then
        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/v1/products")
                .content(objectMapper.writeValueAsBytes(request))
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    @WithCustomMockUser
    fun `상품을 등록할때, 카테고리 번호는 필수로 입력해야 한다`() {
        // given
        val request = ProductRegistrationRequest(
            null,
            BigDecimal("4000.00"),
            BigDecimal("1000.00"),
            "아메리카노",
            "고급 원두로 만든 아메리카노 입니다.",
            "barcode",
            LocalDate.of(2024, 2, 20),
            CafeProductSize.SMALL
        )

        // when then
        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/v1/products")
                .content(objectMapper.writeValueAsBytes(request))
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(MockMvcResultHandlers.print())
            .isInvalidInputValueResponse("카테고리를 선택해주세요.")
    }

    @Test
    @WithCustomMockUser
    fun `상품을 등록할때, 올바른 상품 가격을 입력해야 한다`() {
        // given
        val request1 = ProductRegistrationRequest(
            1L,
            null,
            BigDecimal("1000.00"),
            "아메리카노",
            "고급 원두로 만든 아메리카노 입니다.",
            "barcode",
            LocalDate.of(2024, 2, 20),
            CafeProductSize.SMALL
        )
        val request2 = ProductRegistrationRequest(
            1L,
            BigDecimal("-0.1"),
            BigDecimal("1000.00"),
            "아메리카노",
            "고급 원두로 만든 아메리카노 입니다.",
            "barcode",
            LocalDate.of(2024, 2, 20),
            CafeProductSize.SMALL
        )
        val request3 = ProductRegistrationRequest(
            1L,
            BigDecimal("9999999999.1"),
            BigDecimal("1000.00"),
            "아메리카노",
            "고급 원두로 만든 아메리카노 입니다.",
            "barcode",
            LocalDate.of(2024, 2, 20),
            CafeProductSize.SMALL
        )
        val request4 = mapOf(
            "categoryId" to 1L,
            "sellingPrice" to 9999999999.1,
            "costPrice" to "1000",
            "productName" to "아메리카노",
            "description" to "고급 원두로 만든 아메리카노 입니다.",
            "barcode" to "barcode",
            "expirationDate" to "2024-01-01",
            "size" to "SMALL",
        )
        val request5 = mapOf(
            "categoryId" to 1L,
            "sellingPrice" to "-0.1",
            "costPrice" to "1000",
            "productName" to "아메리카노",
            "description" to "고급 원두로 만든 아메리카노 입니다.",
            "barcode" to "barcode",
            "expirationDate" to "2024-01-01",
            "size" to "SMALL",
        )

        // when then
        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/v1/products")
                .content(objectMapper.writeValueAsBytes(request1))
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(MockMvcResultHandlers.print())
            .isInvalidInputValueResponse("상품 가격을 입력해주세요.")
        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/v1/products")
                .content(objectMapper.writeValueAsBytes(request2))
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(MockMvcResultHandlers.print())
            .isInvalidInputValueResponse("상품 가격은 0 ~ 9,999,999,999 까지 입력해주세요.")
        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/v1/products")
                .content(objectMapper.writeValueAsBytes(request3))
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(MockMvcResultHandlers.print())
            .isInvalidInputValueResponse("상품 가격은 0 ~ 9,999,999,999 까지 입력해주세요.")
        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/v1/products")
                .content(objectMapper.writeValueAsBytes(request4))
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(MockMvcResultHandlers.print())
            .isInvalidInputValueResponse("상품 가격은 0 ~ 9,999,999,999 까지 입력해주세요.")
        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/v1/products")
                .content(objectMapper.writeValueAsBytes(request5))
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(MockMvcResultHandlers.print())
            .isInvalidInputValueResponse("상품 가격은 0 ~ 9,999,999,999 까지 입력해주세요.")
    }

    @Test
    @WithCustomMockUser
    fun `상품을 등록할때, 올바른 원가 입력해야 한다`() {
        // given
        val request1 = ProductRegistrationRequest(
            1L,
            BigDecimal("4000.00"),
            null,
            "아메리카노",
            "고급 원두로 만든 아메리카노 입니다.",
            "barcode",
            LocalDate.of(2024, 2, 20),
            CafeProductSize.SMALL
        )
        val request2 = ProductRegistrationRequest(
            1L,
            BigDecimal("4000.00"),
            BigDecimal("-0.1"),
            "아메리카노",
            "고급 원두로 만든 아메리카노 입니다.",
            "barcode",
            LocalDate.of(2024, 2, 20),
            CafeProductSize.SMALL
        )
        val request3 = ProductRegistrationRequest(
            1L,
            BigDecimal("4000.00"),
            BigDecimal("9999999999.1"),
            "아메리카노",
            "고급 원두로 만든 아메리카노 입니다.",
            "barcode",
            LocalDate.of(2024, 2, 20),
            CafeProductSize.SMALL
        )
        val request4 = mapOf(
            "categoryId" to 1L,
            "sellingPrice" to "4000",
            "costPrice" to 9999999999.1,
            "productName" to "아메리카노",
            "description" to "고급 원두로 만든 아메리카노 입니다.",
            "barcode" to "barcode",
            "expirationDate" to "2024-01-01",
            "size" to "SMALL",
        )
        val request5 = mapOf(
            "categoryId" to 1L,
            "sellingPrice" to "4000",
            "costPrice" to "-0.1",
            "productName" to "아메리카노",
            "description" to "고급 원두로 만든 아메리카노 입니다.",
            "barcode" to "barcode",
            "expirationDate" to "2024-01-01",
            "size" to "SMALL",
        )

        // when then
        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/v1/products")
                .content(objectMapper.writeValueAsBytes(request1))
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(MockMvcResultHandlers.print())
            .isInvalidInputValueResponse("원가를 입력해주세요.")
        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/v1/products")
                .content(objectMapper.writeValueAsBytes(request2))
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(MockMvcResultHandlers.print())
            .isInvalidInputValueResponse("원가는 0 ~ 9,999,999,999 까지 입력해주세요.")
        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/v1/products")
                .content(objectMapper.writeValueAsBytes(request3))
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(MockMvcResultHandlers.print())
            .isInvalidInputValueResponse("원가는 0 ~ 9,999,999,999 까지 입력해주세요.")
        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/v1/products")
                .content(objectMapper.writeValueAsBytes(request4))
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(MockMvcResultHandlers.print())
            .isInvalidInputValueResponse("원가는 0 ~ 9,999,999,999 까지 입력해주세요.")
        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/v1/products")
                .content(objectMapper.writeValueAsBytes(request5))
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(MockMvcResultHandlers.print())
            .isInvalidInputValueResponse("원가는 0 ~ 9,999,999,999 까지 입력해주세요.")
    }

    @Test
    @WithCustomMockUser
    fun `상품을 등록할때, 상품 이름은 필수로 입력해야 한다`() {
        // given
        val request1 = ProductRegistrationRequest(
            1L,
            BigDecimal("4000.00"),
            BigDecimal("1000.00"),
            "",
            "고급 원두로 만든 아메리카노 입니다.",
            "barcode",
            LocalDate.of(2024, 2, 20),
            CafeProductSize.SMALL
        )
        val request2 = ProductRegistrationRequest(
            1L,
            BigDecimal("4000.00"),
            BigDecimal("1000.00"),
            null,
            "고급 원두로 만든 아메리카노 입니다.",
            "barcode",
            LocalDate.of(2024, 2, 20),
            CafeProductSize.SMALL
        )

        // when then
        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/v1/products")
                .content(objectMapper.writeValueAsBytes(request1))
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(MockMvcResultHandlers.print())
            .isInvalidInputValueResponse("상품 이름을 입력해주세요.")
        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/v1/products")
                .content(objectMapper.writeValueAsBytes(request2))
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(MockMvcResultHandlers.print())
            .isInvalidInputValueResponse("상품 이름을 입력해주세요.")
    }

    @Test
    @WithCustomMockUser
    fun `상품을 등록할때, 상품 설명은 필수로 입력해야 한다`() {
        // given
        val request1 = ProductRegistrationRequest(
            1L,
            BigDecimal("4000.00"),
            BigDecimal("1000.00"),
            "아메리카노",
            "",
            "barcode",
            LocalDate.of(2024, 2, 20),
            CafeProductSize.SMALL
        )
        val request2 = ProductRegistrationRequest(
            1L,
            BigDecimal("4000.00"),
            BigDecimal("1000.00"),
            "아메리카노",
            null,
            "barcode",
            LocalDate.of(2024, 2, 20),
            CafeProductSize.SMALL
        )

        // when then
        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/v1/products")
                .content(objectMapper.writeValueAsBytes(request1))
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(MockMvcResultHandlers.print())
            .isInvalidInputValueResponse("상품 설명을 입력해주세요.")
        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/v1/products")
                .content(objectMapper.writeValueAsBytes(request2))
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(MockMvcResultHandlers.print())
            .isInvalidInputValueResponse("상품 설명을 입력해주세요.")
    }

    @Test
    @WithCustomMockUser
    fun `상품을 등록할때, 바코드 필수로 입력해야 한다`() {
        // given
        val request1 = ProductRegistrationRequest(
            1L,
            BigDecimal("4000.00"),
            BigDecimal("1000.00"),
            "아메리카노",
            "고급 원두로 만든 아메리카노 입니다.",
            "",
            LocalDate.of(2024, 2, 20),
            CafeProductSize.SMALL
        )
        val request2 = ProductRegistrationRequest(
            1L,
            BigDecimal("4000.00"),
            BigDecimal("1000.00"),
            "아메리카노",
            "고급 원두로 만든 아메리카노 입니다.",
            null,
            LocalDate.of(2024, 2, 20),
            CafeProductSize.SMALL
        )

        // when then
        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/v1/products")
                .content(objectMapper.writeValueAsBytes(request1))
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(MockMvcResultHandlers.print())
            .isInvalidInputValueResponse("바코드를 입력해주세요.")
        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/v1/products")
                .content(objectMapper.writeValueAsBytes(request2))
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(MockMvcResultHandlers.print())
            .isInvalidInputValueResponse("바코드를 입력해주세요.")
    }

    @Test
    @WithCustomMockUser
    fun `상품을 등록할때, 올바른 유통기한을 입력해야 한다`() {
        // given
        val request1 = ProductRegistrationRequest(
            1L,
            BigDecimal("4000.00"),
            BigDecimal("1000.00"),
            "아메리카노",
            "고급 원두로 만든 아메리카노 입니다.",
            "barcode",
            null,
            CafeProductSize.SMALL
        )
        val request2 = mapOf(
            "categoryId" to 1L,
            "sellingPrice" to "4000",
            "costPrice" to "1000",
            "productName" to "아메리카노",
            "description" to "고급 원두로 만든 아메리카노 입니다.",
            "barcode" to "barcode",
            "expirationDate" to "20240101",
            "size" to "SMALL",
        )

        // when then
        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/v1/products")
                .content(objectMapper.writeValueAsBytes(request1))
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(MockMvcResultHandlers.print())
            .isInvalidInputValueResponse("유통기한을 입력해주세요.")
        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/v1/products")
                .content(objectMapper.writeValueAsBytes(request2))
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(MockMvcResultHandlers.print())
            .isInvalidInputValueResponse(ErrorCode.NO_CONTENT_HTTP_BODY.message)
    }

    @Test
    @WithCustomMockUser
    fun `상품을 등록할때, 상품 사이즈를 입력해야 한다`() {
        // given
        val request1 = ProductRegistrationRequest(
            1L,
            BigDecimal("4000.00"),
            BigDecimal("1000.00"),
            "아메리카노",
            "고급 원두로 만든 아메리카노 입니다.",
            "barcode",
            LocalDate.of(2024, 2, 20),
            null
        )
        val request2 = mapOf(
            "categoryId" to 1L,
            "sellingPrice" to "4000",
            "costPrice" to "1000",
            "productName" to "아메리카노",
            "description" to "고급 원두로 만든 아메리카노 입니다.",
            "barcode" to "barcode",
            "expirationDate" to "2024-01-01",
            "size" to "SMAL",
        )
        val request3 = mapOf(
            "categoryId" to 1L,
            "sellingPrice" to "4000",
            "costPrice" to "1000",
            "productName" to "아메리카노",
            "description" to "고급 원두로 만든 아메리카노 입니다.",
            "barcode" to "barcode",
            "expirationDate" to "2024-01-01",
            "size" to "OWNER",
        )

        // when then
        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/v1/products")
                .content(objectMapper.writeValueAsBytes(request1))
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(MockMvcResultHandlers.print())
            .isInvalidInputValueResponse("잘못된 상품 사이즈 입니다.")
        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/v1/products")
                .content(objectMapper.writeValueAsBytes(request2))
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(MockMvcResultHandlers.print())
            .isInvalidInputValueResponse(ErrorCode.NO_CONTENT_HTTP_BODY.message)
        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/v1/products")
                .content(objectMapper.writeValueAsBytes(request3))
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(MockMvcResultHandlers.print())
            .isInvalidInputValueResponse(ErrorCode.NO_CONTENT_HTTP_BODY.message)
    }

    @Test
    @WithCustomMockUser
    fun `상품을 수정한다`() {
        // given
        val request = ProductUpdateRequest(
            1L,
            1L,
            BigDecimal("4000.00"),
            BigDecimal("1000.00"),
            "아메리카노",
            "고급 원두로 만든 아메리카노 입니다.",
            "barcode",
            LocalDate.of(2024, 2, 20),
            CafeProductSize.SMALL
        )

        justRun { productService.updateProduct(request) }

        // when then
        mockMvc.perform(
            MockMvcRequestBuilders.patch("/api/v1/products")
                .content(objectMapper.writeValueAsBytes(request))
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    @WithCustomMockUser
    fun `상품을 수정할때, 상품 번호를 필수로 입력해야 한다`() {
        // given
        val request = ProductUpdateRequest(
            null,
            1L,
            null,
            null,
            "아메리카노",
            "고급 원두로 만든 아메리카노 입니다.",
            "barcode",
            null,
            null
        )

        // when then
        mockMvc.perform(
            MockMvcRequestBuilders.patch("/api/v1/products")
                .content(objectMapper.writeValueAsBytes(request))
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(MockMvcResultHandlers.print())
            .isInvalidInputValueResponse("상품 ID 를 입력해주세요.")
    }

    @Test
    @WithCustomMockUser
    fun `상품을 수정할때, 상품 가격을 수정하려면 올바른 범위의 가격이어야 한다`() {
        // given
        val request1 = ProductUpdateRequest(
            1L,
            null,
            BigDecimal("-0.1"),
            null,
            "아메리카노",
            "고급 원두로 만든 아메리카노 입니다.",
            "barcode",
            null,
            null
        )
        val request2 = ProductUpdateRequest(
            1L,
            null,
            BigDecimal("9999999999.1"),
            null,
            "아메리카노",
            "고급 원두로 만든 아메리카노 입니다.",
            "barcode",
            null,
            null
        )
        val request3 = mapOf(
            "id" to 1L,
            "sellingPrice" to 9999999999.1,
        )
        val request4 = mapOf(
            "id" to 1L,
            "sellingPrice" to "-0.1",
        )

        // when then
        mockMvc.perform(
            MockMvcRequestBuilders.patch("/api/v1/products")
                .content(objectMapper.writeValueAsBytes(request1))
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(MockMvcResultHandlers.print())
            .isInvalidInputValueResponse("상품 가격은 0 ~ 9,999,999,999 까지 입력해주세요.")
        mockMvc.perform(
            MockMvcRequestBuilders.patch("/api/v1/products")
                .content(objectMapper.writeValueAsBytes(request2))
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(MockMvcResultHandlers.print())
            .isInvalidInputValueResponse("상품 가격은 0 ~ 9,999,999,999 까지 입력해주세요.")
        mockMvc.perform(
            MockMvcRequestBuilders.patch("/api/v1/products")
                .content(objectMapper.writeValueAsBytes(request3))
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(MockMvcResultHandlers.print())
            .isInvalidInputValueResponse("상품 가격은 0 ~ 9,999,999,999 까지 입력해주세요.")
        mockMvc.perform(
            MockMvcRequestBuilders.patch("/api/v1/products")
                .content(objectMapper.writeValueAsBytes(request3))
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(MockMvcResultHandlers.print())
            .isInvalidInputValueResponse("상품 가격은 0 ~ 9,999,999,999 까지 입력해주세요.")
    }

    @Test
    @WithCustomMockUser
    fun `상품을 수정할때, 원가를 수정하려면 올바른 범위의 가격이어야 한다`() {
        // given
        val request1 = ProductUpdateRequest(
            1L,
            null,
            null,
            BigDecimal("-0.1"),
            "아메리카노",
            "고급 원두로 만든 아메리카노 입니다.",
            "barcode",
            null,
            null
        )
        val request2 = ProductUpdateRequest(
            1L,
            null,
            null,
            BigDecimal("9999999999.1"),
            "아메리카노",
            "고급 원두로 만든 아메리카노 입니다.",
            "barcode",
            null,
            null
        )
        val request3 = mapOf(
            "id" to 1L,
            "costPrice" to 9999999999.1,
        )
        val request4 = mapOf(
            "id" to 1L,
            "costPrice" to "-0.1",
        )

        // when then
        mockMvc.perform(
            MockMvcRequestBuilders.patch("/api/v1/products")
                .content(objectMapper.writeValueAsBytes(request1))
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(MockMvcResultHandlers.print())
            .isInvalidInputValueResponse("원가는 0 ~ 9,999,999,999 까지 입력해주세요.")
        mockMvc.perform(
            MockMvcRequestBuilders.patch("/api/v1/products")
                .content(objectMapper.writeValueAsBytes(request2))
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(MockMvcResultHandlers.print())
            .isInvalidInputValueResponse("원가는 0 ~ 9,999,999,999 까지 입력해주세요.")
        mockMvc.perform(
            MockMvcRequestBuilders.patch("/api/v1/products")
                .content(objectMapper.writeValueAsBytes(request3))
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(MockMvcResultHandlers.print())
            .isInvalidInputValueResponse("원가는 0 ~ 9,999,999,999 까지 입력해주세요.")
        mockMvc.perform(
            MockMvcRequestBuilders.patch("/api/v1/products")
                .content(objectMapper.writeValueAsBytes(request4))
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(MockMvcResultHandlers.print())
            .isInvalidInputValueResponse("원가는 0 ~ 9,999,999,999 까지 입력해주세요.")
    }

    @Test
    @WithCustomMockUser
    fun `상품을 수정할때, 유통기한을 수정하려면 yyyy-MM-dd 형식이어야 한다`() {
        // given
        val request = mapOf(
            "id" to 1L,
            "expirationDate" to "20240101",
        )

        // when then
        mockMvc.perform(
            MockMvcRequestBuilders.patch("/api/v1/products")
                .content(objectMapper.writeValueAsBytes(request))
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(MockMvcResultHandlers.print())
            .isInvalidInputValueResponse(ErrorCode.NO_CONTENT_HTTP_BODY.message)
    }

    @Test
    @WithCustomMockUser
    fun `상품을 수정할때, 상품 크기를 변경하려면 올바른 형식이어야 한다`() {
        // given
        val request1 = mapOf(
            "id" to 1L,
            "size" to "small",
        )
        val request2 = mapOf(
            "id" to 1L,
            "size" to "owner",
        )

        // when then
        mockMvc.perform(
            MockMvcRequestBuilders.patch("/api/v1/products")
                .content(objectMapper.writeValueAsBytes(request1))
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(MockMvcResultHandlers.print())
            .isInvalidInputValueResponse(ErrorCode.NO_CONTENT_HTTP_BODY.message)
        mockMvc.perform(
            MockMvcRequestBuilders.patch("/api/v1/products")
                .content(objectMapper.writeValueAsBytes(request2))
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(MockMvcResultHandlers.print())
            .isInvalidInputValueResponse(ErrorCode.NO_CONTENT_HTTP_BODY.message)
    }

    @Test
    @WithCustomMockUser
    fun `상품을 삭제한다`() {
        // given
        val request = ProductDeleteRequest(1L)

        justRun { productService.deleteProduct(request) }

        // when then
        mockMvc.perform(
            MockMvcRequestBuilders.delete("/api/v1/products")
                .content(objectMapper.writeValueAsBytes(request))
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    @WithCustomMockUser
    fun `상품을 삭제할때, 상품 번호를 필수로 입력해야 한다`() {
        // given
        val request = ProductDeleteRequest(null)

        // when then
        mockMvc.perform(
            MockMvcRequestBuilders.delete("/api/v1/products")
                .content(objectMapper.writeValueAsBytes(request))
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(MockMvcResultHandlers.print())
            .isInvalidInputValueResponse("상품 ID 를 입력해주세요.")
    }

    @Test
    @WithCustomMockUser
    fun `페이지 기반으로 상품 목록을 조회한다`() {
        // given
        val page = 1
        val size = 10

        every { productService.getProductsByPage(any<ProductListRequest>()) } returns mockk(relaxed = true)

        // when then
        mockMvc.perform(
            MockMvcRequestBuilders.get("/api/v1/products")
                .param("page", page.toString())
                .param("size", size.toString()))
            .andDo(MockMvcResultHandlers.print())
            .isOkBaseResponse()
            .andExpect(MockMvcResultMatchers.jsonPath("$.data").isNotEmpty)
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.products").isArray)
    }

    @Test
    @WithCustomMockUser
    fun `상품을 상세 조회한다`() {
        // given
        val id = 1

        every { productService.getProductDetail(any<ProductDetailRequest>()) } returns mockk(relaxed = true)

        // when then
        mockMvc.perform(
            MockMvcRequestBuilders.get("/api/v1/products/$id"))
            .andDo(MockMvcResultHandlers.print())
            .isOkBaseResponse()
            .andExpect(MockMvcResultMatchers.jsonPath("$.data").isNotEmpty)
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.product").isMap)
    }

    @Test
    @WithCustomMockUser
    fun `검색어로 상품 목록을 검색한다`() {
        // given
        val query = "ㅅㅋㄹ"

        every { productService.searchProducts(any<ProductSearchRequest>()) } returns mockk(relaxed = true)

        // when then
        mockMvc.perform(
            MockMvcRequestBuilders.get("/api/v1/products/search")
                .param("query", query))
            .andDo(MockMvcResultHandlers.print())
            .isOkBaseResponse()
            .andExpect(MockMvcResultMatchers.jsonPath("$.data").isNotEmpty)
            .andExpect(MockMvcResultMatchers.jsonPath("$.data.products").isArray)
    }

    @Test
    @WithCustomMockUser
    fun `검색어로 상품 목록을 검색할때 검색어는 필수로 입력해야 한다`() {
        // given
        val query1 = ""
        val query2 = null

        // when then
        mockMvc.perform(
            MockMvcRequestBuilders.get("/api/v1/products/search")
                .param("query", query1))
            .andDo(MockMvcResultHandlers.print())
            .isInvalidInputValueResponse("검색어를 입력해주세요.")
        mockMvc.perform(
            MockMvcRequestBuilders.get("/api/v1/products/search")
                .param("query", query2))
            .andDo(MockMvcResultHandlers.print())
            .isInvalidInputValueResponse("검색어를 입력해주세요.")
    }

}