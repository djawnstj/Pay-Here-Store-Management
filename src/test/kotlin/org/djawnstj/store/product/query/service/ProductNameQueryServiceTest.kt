package org.djawnstj.store.product.query.service

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.djawnstj.store.product.dto.ProductDto
import org.djawnstj.store.product.query.dto.ProductNameQueryDto
import org.djawnstj.store.product.query.entity.ProductNameQuery
import org.djawnstj.store.product.query.repository.ProductNameQueryRepository
import org.djawnstj.store.product.query.util.ProductNameQueryUtil
import org.junit.jupiter.api.Test

class ProductNameQueryServiceTest {

    private val productNameQueryRepository: ProductNameQueryRepository = mockk()
    private val productNameQueryUtil: ProductNameQueryUtil = mockk()

    private val productNameQueryService = ProductNameQueryService(productNameQueryRepository, productNameQueryUtil)

    @Test
    fun `상품 이름에 한글이 있는 경우 한글의 초성을 추출해 쿼리로 저장한다`() {
        // given
        val product: ProductDto = mockk(relaxed = true)

        every { productNameQueryUtil.hasKoreanInitials(product.productName) } returns true
        every { productNameQueryUtil.extractKoreanInitialsQuery(product.productName) } returns "query"
        every { productNameQueryRepository.save(any<ProductNameQuery>()) } answers { it.invocation.args[0] as ProductNameQuery }

        // when
        productNameQueryService.saveQuery(product)

        // then
        verify(exactly = 1) { productNameQueryRepository.save(any<ProductNameQuery>()) }
    }

    @Test
    fun `상품 이름에 한글이 없는 경우 상품명 인덱스를 저장하지 않는다`() {
        // given
        val product: ProductDto = mockk(relaxed = true)

        every { productNameQueryUtil.hasKoreanInitials(product.productName) } returns false

        // when
        productNameQueryService.saveQuery(product)

        // then
        verify(exactly = 0) { productNameQueryRepository.save(any<ProductNameQuery>()) }
    }

    @Test
    fun `검색어가 한글 초성으로만 이루어진 경우 해당 검색어로 상품명 인덱스를 검색한다`() {
        // given
        val query = "query"
        val productNameQueries: List<ProductNameQuery> = mockk(relaxed = true)

        every { productNameQueryUtil.isOnlyKoreanInitials(query) } returns true
        every { productNameQueryRepository.findByNameQueryContaining(query) } returns productNameQueries

        // when
        productNameQueryService.searchByKoreanInitialsQuery(query)

        // then
        verify(exactly = 1) { productNameQueryRepository.findByNameQueryContaining(query) }
    }

    @Test
    fun `검색어가 한글 초성으로만 이루어지지 않은 경우 검색어로 상품명 인덱스를 검색하지 않는다`() {
        // given
        val query = "query"

        every { productNameQueryUtil.isOnlyKoreanInitials(query) } returns false

        // when
        productNameQueryService.searchByKoreanInitialsQuery(query)

        // then
        verify(exactly = 0) { productNameQueryRepository.findByNameQueryContaining(query) }
    }

}