package org.djawnstj.store.product.query.repository

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.tuple
import org.djawnstj.store.IntegrationTestSupport
import org.djawnstj.store.product.query.entity.ProductNameQuery
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

class ProductNameQueryRepositoryTest: IntegrationTestSupport() {

    @Autowired
    private lateinit var productNameQueryRepository: ProductNameQueryRepository

    @AfterEach
    fun tearDown() {
        productNameQueryRepository.deleteAll()
    }

    @Test
    fun `특정 문자열이 포함된 productNameQuery 를 조회한다`() {
        // given
        val productNameQuery1 = ProductNameQuery(1, "슈크림 라떼")
        val productNameQuery2 = ProductNameQuery(2, "바닐라 라떼")
        val productNameQuery3 = ProductNameQuery(3, "카페 라떼")
        val productNameQuery4 = ProductNameQuery(4, "녹차 라떼")
        val productNameQuery5 = ProductNameQuery(5, "아메리카노")

        productNameQueryRepository.saveAll(listOf(productNameQuery1, productNameQuery2, productNameQuery3, productNameQuery4, productNameQuery5))

        // when
        val result = productNameQueryRepository.findByNameQueryContaining("라떼")

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

}