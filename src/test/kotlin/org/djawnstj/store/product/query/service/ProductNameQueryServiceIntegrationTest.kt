package org.djawnstj.store.product.query.service

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.tuple
import org.djawnstj.store.IntegrationTestSupport
import org.djawnstj.store.product.dto.ProductCategoryDto
import org.djawnstj.store.product.dto.ProductDto
import org.djawnstj.store.product.entity.CafeProductSize
import org.djawnstj.store.product.entity.Product
import org.djawnstj.store.product.entity.ProductCategory
import org.djawnstj.store.product.query.entity.ProductNameQuery
import org.djawnstj.store.product.query.repository.ProductNameQueryRepository
import org.djawnstj.store.product.repository.ProductCategoryJpaRepository
import org.djawnstj.store.product.repository.ProductJpaRepository
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import java.math.BigDecimal
import java.time.LocalDate

class ProductNameQueryServiceIntegrationTest: IntegrationTestSupport() {

    @Autowired
    private lateinit var productNameQueryService: ProductNameQueryService

    @Autowired
    private lateinit var productCategoryJpaRepository: ProductCategoryJpaRepository
    @Autowired
    private lateinit var productJpaRepository: ProductJpaRepository
    @Autowired
    private lateinit var productNameQueryRepository: ProductNameQueryRepository

    @AfterEach
    fun tearDown() {
        productCategoryJpaRepository.deleteAll()
        productJpaRepository.deleteAll()
        productNameQueryRepository.deleteAll()
    }

    @Test
    fun `상품을 전달받아 상품명의 한글 초성을 추출하여 인덱스를 저장한다`() {
        // given
        val product = ProductDto(
            1,
            ProductCategoryDto(1, "커피"),
            BigDecimal("4000.00"),
            BigDecimal("1000.00"),
            "아메리카노",
            "고급 원두로 만든 아메리카노 입니다.",
            "barcode",
            LocalDate.of(2024, 2, 20),
            CafeProductSize.SMALL)

        // when
        productNameQueryService.saveQuery(product)

        // then
        val result = productNameQueryRepository.findByIdOrNull(product.id)
        assertThat(result).isNotNull
        assertThat(result!!.nameQuery).isEqualTo("ㅇㅁㄹㅋㄴ")
    }

    @Test
    fun `한글 초성으로 상품명 인덱스를 조회한다`() {
        // given
        val productNameQuery1 = ProductNameQuery(1, "ㅅㅋㄹ ㄹㄸ")
        val productNameQuery2 = ProductNameQuery(2, "ㅂㄴㄹ ㄹㄸ")
        val productNameQuery3 = ProductNameQuery(3, "ㅋㅍ ㄹㄸ")
        val productNameQuery4 = ProductNameQuery(4, "ㄴㅊ ㄹㄸ")
        val productNameQuery5 = ProductNameQuery(5, "ㅇㅁㄹㅋㄴ")

        productNameQueryRepository.saveAll(listOf(productNameQuery1, productNameQuery2, productNameQuery3, productNameQuery4, productNameQuery5))

        // when
        val result = productNameQueryService.searchByKoreanInitialsQuery("ㄹㄸ")

        // then
        assertThat(result).hasSize(4)
            .extracting("id", "nameQuery")
            .containsExactlyInAnyOrder(
                tuple(productNameQuery1.id, productNameQuery1.nameQuery),
                tuple(productNameQuery2.id, productNameQuery2.nameQuery),
                tuple(productNameQuery3.id, productNameQuery3.nameQuery),
                tuple(productNameQuery4.id, productNameQuery4.nameQuery),
            )
    }

    @Test
    fun `검색어가 한글 초성으로만 이루어지지 않은 경우 빈 리스트를 반환한다`() {
        // given
        val productNameQuery1 = ProductNameQuery(1, "슈크림 라떼")
        val productNameQuery2 = ProductNameQuery(2, "바닐라 라떼")
        val productNameQuery3 = ProductNameQuery(3, "카페 라떼")
        val productNameQuery4 = ProductNameQuery(4, "녹차 라떼")
        val productNameQuery5 = ProductNameQuery(5, "아메리카노")

        productNameQueryRepository.saveAll(listOf(productNameQuery1, productNameQuery2, productNameQuery3, productNameQuery4, productNameQuery5))

        // when
        val result = productNameQueryService.searchByKoreanInitialsQuery("라떼")

        // then
        assertThat(result).hasSize(0)
    }

}