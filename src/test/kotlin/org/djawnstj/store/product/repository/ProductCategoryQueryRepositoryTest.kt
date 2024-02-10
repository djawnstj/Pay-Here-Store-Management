package org.djawnstj.store.product.repository

import org.assertj.core.api.Assertions.assertThat
import org.djawnstj.store.IntegrationTestSupport
import org.djawnstj.store.product.entity.ProductCategory
import org.junit.jupiter.api.*
import org.springframework.beans.factory.annotation.Autowired

@TestMethodOrder(
    MethodOrderer.OrderAnnotation::class)
class ProductCategoryQueryRepositoryTest: IntegrationTestSupport() {

    @Autowired
    private lateinit var productCategoryQueryRepository: ProductCategoryQueryRepository

    @Autowired
    private lateinit var productCategoryJpaRepository: ProductCategoryJpaRepository

    @AfterEach
    fun tearDown() {
        productCategoryJpaRepository.deleteAll()
    }

    @Order(1)
    @Test
    fun `상품 카테고리 이름으로 카테고리를 조회한다`() {
        // given
        val name = "커피"
        val productCategory = productCategoryJpaRepository.save(ProductCategory(name))

        // when
        val result = productCategoryQueryRepository.findByName(name)

        // then
        assertThat(result).isNotNull
        assertThat(result!!.id).isEqualTo(productCategory.id)
        assertThat(result.categoryName).isEqualTo(name)
    }

    @Order(2)
    @Test
    fun `상품 카테고리 이름으로 카테고리를 조회할 때, 같은 이름을 가진 카테고리가 없다면 null 을 반환한다`() {
        // when
        val result = productCategoryQueryRepository.findByName("커피")

        // then
        assertThat(result).isNull()
    }

    @Order(3)
    @Test
    fun `상품 카테고리 id로 카테고리를 조회한다`() {
        // given
        val name = "커피"
        val productCategory = productCategoryJpaRepository.save(ProductCategory(name))

        // when
        val result = productCategoryQueryRepository.findById(productCategory.id)

        // then
        assertThat(result).isNotNull
        assertThat(result!!.id).isEqualTo(productCategory.id)
        assertThat(result.categoryName).isEqualTo(name)
    }

    @Order(4)
    @Test
    fun `상품 카테고리 id로 카테고리를 조회할 때, 같은 id를 가진 카테고리가 없다면 null 을 반환한다`() {
        // when
        val result = productCategoryQueryRepository.findById(1L)

        // then
        assertThat(result).isNull()
    }

}