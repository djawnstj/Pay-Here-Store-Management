package org.djawnstj.store.product.repository

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.tuple
import org.djawnstj.store.IntegrationTestSupport
import org.djawnstj.store.product.entity.CafeProductSize
import org.djawnstj.store.product.entity.Product
import org.djawnstj.store.product.entity.ProductCategory
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import java.math.BigDecimal
import java.time.LocalDate

class ProductQueryRepositoryTest: IntegrationTestSupport() {

    @Autowired
    private lateinit var productQueryRepository: ProductQueryRepository

    @Autowired
    private lateinit var productCategoryJpaRepository: ProductCategoryJpaRepository
    @Autowired
    private lateinit var productJpaRepository: ProductJpaRepository

    @AfterEach
    fun tearDown() {
        productJpaRepository.deleteAll()
        productCategoryJpaRepository.deleteAll()
    }

    @Test
    fun `상품 id로 상품을 조회한다`() {
        // given
        val category = productCategoryJpaRepository.save(ProductCategory("커피"))
        val product = productJpaRepository.save(
            Product(
                category,
                BigDecimal("4000.00"),
                BigDecimal("1000.00"),
                "아메리카노",
                "고급 원두로 만든 아메리카노 입니다.",
                "barcode",
                LocalDate.of(2024, 2, 20),
                CafeProductSize.SMALL
            )
        )

        // when
        val result = productQueryRepository.findById(product.id)

        // then
        assertThat(result).isNotNull
        assertThat(result)
            .extracting("sellingPrice", "costPrice", "productName", "description", "barcode", "expirationDate", "size")
            .contains(product.sellingPrice, product.costPrice, product.productName, product.description, product.barcode, product.expirationDate, product.size)
    }

    @Test
    fun `상품 id로 상품을 조회할 때, 해당하는 id 를 가진 상품이 없다면 null 을 반환한다`() {
        // when
        val result = productQueryRepository.findById(1L)

        // then
        assertThat(result).isNull()
    }

    @Test
    fun `page 기반으로 상품 목록을 조회한다`() {
        // given
        val category = productCategoryJpaRepository.save(ProductCategory("커피"))
        val product1 = productJpaRepository.save(
            Product(
                category,
                BigDecimal("4000.00"),
                BigDecimal("1000.00"),
                "아메리카노1",
                "고급 원두로 만든 아메리카노1 입니다.",
                "barcode",
                LocalDate.of(2024, 2, 20),
                CafeProductSize.SMALL
            )
        )
        val product2 = productJpaRepository.save(
            Product(
                category,
                BigDecimal("4000.00"),
                BigDecimal("1000.00"),
                "아메리카노2",
                "고급 원두로 만든 아메리카노2 입니다.",
                "barcode",
                LocalDate.of(2024, 2, 20),
                CafeProductSize.SMALL
            )
        )
        val product3 = productJpaRepository.save(
            Product(
                category,
                BigDecimal("4000.00"),
                BigDecimal("1000.00"),
                "아메리카노3",
                "고급 원두로 만든 아메리카노3 입니다.",
                "barcode",
                LocalDate.of(2024, 2, 20),
                CafeProductSize.SMALL
            )
        )
        val product4 = productJpaRepository.save(
            Product(
                category,
                BigDecimal("4000.00"),
                BigDecimal("1000.00"),
                "아메리카노4",
                "고급 원두로 만든 아메리카노4 입니다.",
                "barcode",
                LocalDate.of(2024, 2, 20),
                CafeProductSize.SMALL
            )
        )
        val product5 = productJpaRepository.save(
            Product(
                category,
                BigDecimal("4000.00"),
                BigDecimal("1000.00"),
                "아메리카노5",
                "고급 원두로 만든 아메리카노5 입니다.",
                "barcode",
                LocalDate.of(2024, 2, 20),
                CafeProductSize.SMALL
            )
        )
        val product6 = productJpaRepository.save(
            Product(
                category,
                BigDecimal("4000.00"),
                BigDecimal("1000.00"),
                "아메리카노6",
                "고급 원두로 만든 아메리카노6 입니다.",
                "barcode",
                LocalDate.of(2024, 2, 20),
                CafeProductSize.SMALL
            )
        )
        val product7 = productJpaRepository.save(
            Product(
                category,
                BigDecimal("4000.00"),
                BigDecimal("1000.00"),
                "아메리카노7",
                "고급 원두로 만든 아메리카노7 입니다.",
                "barcode",
                LocalDate.of(2024, 2, 20),
                CafeProductSize.SMALL
            )
        )
        val product8 = productJpaRepository.save(
            Product(
                category,
                BigDecimal("4000.00"),
                BigDecimal("1000.00"),
                "아메리카노8",
                "고급 원두로 만든 아메리카노8 입니다.",
                "barcode",
                LocalDate.of(2024, 2, 20),
                CafeProductSize.SMALL
            )
        )
        val product9 = productJpaRepository.save(
            Product(
                category,
                BigDecimal("4000.00"),
                BigDecimal("1000.00"),
                "아메리카노9",
                "고급 원두로 만든 아메리카노9 입니다.",
                "barcode",
                LocalDate.of(2024, 2, 20),
                CafeProductSize.SMALL
            )
        )
        val product10 = productJpaRepository.save(
            Product(
                category,
                BigDecimal("4000.00"),
                BigDecimal("1000.00"),
                "아메리카노10",
                "고급 원두로 만든 아메리카노10 입니다.",
                "barcode",
                LocalDate.of(2024, 2, 20),
                CafeProductSize.SMALL
            )
        )

        // when
        val result = productQueryRepository.findAll(PageRequest.of(1, 5))

        // then
        assertThat(result).hasSize(5)
            .extracting("id", "sellingPrice", "costPrice", "productName", "description", "barcode", "expirationDate", "size")
            .containsExactlyInAnyOrder(
                tuple(product6.id, product6.sellingPrice, product6.costPrice, product6.productName, product6.description, product6.barcode, product6.expirationDate, product6.size),
                tuple(product7.id, product7.sellingPrice, product7.costPrice, product7.productName, product7.description, product7.barcode, product7.expirationDate, product7.size),
                tuple(product8.id, product8.sellingPrice, product8.costPrice, product8.productName, product8.description, product8.barcode, product8.expirationDate, product8.size),
                tuple(product9.id, product9.sellingPrice, product9.costPrice, product9.productName, product9.description, product9.barcode, product9.expirationDate, product9.size),
                tuple(product10.id, product10.sellingPrice, product10.costPrice, product10.productName, product10.description, product10.barcode, product10.expirationDate, product10.size),
            )
    }

    @Test
    fun `상품 이름을 like 조건으로 상품 목록을 조회한다`() {
        // given
        val category = productCategoryJpaRepository.save(ProductCategory("커피"))
        val product1 = productJpaRepository.save(
            Product(
                category,
                BigDecimal("4000.00"),
                BigDecimal("1000.00"),
                "아메리카노1",
                "고급 원두로 만든 아메리카노1 입니다.",
                "barcode",
                LocalDate.of(2024, 2, 20),
                CafeProductSize.SMALL
            )
        )
        val product2 = productJpaRepository.save(
            Product(
                category,
                BigDecimal("4000.00"),
                BigDecimal("1000.00"),
                "아메리카노2",
                "고급 원두로 만든 아메리카노2 입니다.",
                "barcode",
                LocalDate.of(2024, 2, 20),
                CafeProductSize.SMALL
            )
        )
        val product3 = productJpaRepository.save(
            Product(
                category,
                BigDecimal("4000.00"),
                BigDecimal("1000.00"),
                "아메리카노3",
                "고급 원두로 만든 아메리카노3 입니다.",
                "barcode",
                LocalDate.of(2024, 2, 20),
                CafeProductSize.SMALL
            )
        )
        val product4 = productJpaRepository.save(
            Product(
                category,
                BigDecimal("4000.00"),
                BigDecimal("1000.00"),
                "아메리카노4",
                "고급 원두로 만든 아메리카노4 입니다.",
                "barcode",
                LocalDate.of(2024, 2, 20),
                CafeProductSize.SMALL
            )
        )
        val product5 = productJpaRepository.save(
            Product(
                category,
                BigDecimal("4000.00"),
                BigDecimal("1000.00"),
                "아메리카노5",
                "고급 원두로 만든 아메리카노5 입니다.",
                "barcode",
                LocalDate.of(2024, 2, 20),
                CafeProductSize.SMALL
            )
        )
        val product6 = productJpaRepository.save(
            Product(
                category,
                BigDecimal("4000.00"),
                BigDecimal("1000.00"),
                "아메리카노6",
                "고급 원두로 만든 아메리카노6 입니다.",
                "barcode",
                LocalDate.of(2024, 2, 20),
                CafeProductSize.SMALL
            )
        )
        val product7 = productJpaRepository.save(
            Product(
                category,
                BigDecimal("4000.00"),
                BigDecimal("1000.00"),
                "아메리카노7",
                "고급 원두로 만든 아메리카노7 입니다.",
                "barcode",
                LocalDate.of(2024, 2, 20),
                CafeProductSize.SMALL
            )
        )
        val product8 = productJpaRepository.save(
            Product(
                category,
                BigDecimal("4000.00"),
                BigDecimal("1000.00"),
                "카페 라떼1",
                "고급 원두로 만든 카페 라떼1 입니다.",
                "barcode",
                LocalDate.of(2024, 2, 20),
                CafeProductSize.SMALL
            )
        )
        val product9 = productJpaRepository.save(
            Product(
                category,
                BigDecimal("4000.00"),
                BigDecimal("1000.00"),
                "카페 라떼2",
                "고급 원두로 만든 카페 라떼2 입니다.",
                "barcode",
                LocalDate.of(2024, 2, 20),
                CafeProductSize.SMALL
            )
        )
        val product10 = productJpaRepository.save(
            Product(
                category,
                BigDecimal("4000.00"),
                BigDecimal("1000.00"),
                "카페 라떼3",
                "고급 원두로 만든 카페 라떼3 입니다.",
                "barcode",
                LocalDate.of(2024, 2, 20),
                CafeProductSize.SMALL
            )
        )

        // when
        val result = productQueryRepository.findAllByNameLike("리카")

        // then
        assertThat(result).hasSize(7)
            .extracting("id", "sellingPrice", "costPrice", "productName", "description", "barcode", "expirationDate", "size")
            .containsExactlyInAnyOrder(
                tuple(product1.id, product1.sellingPrice, product1.costPrice, product1.productName, product1.description, product1.barcode, product1.expirationDate, product1.size),
                tuple(product2.id, product2.sellingPrice, product2.costPrice, product2.productName, product2.description, product2.barcode, product2.expirationDate, product2.size),
                tuple(product3.id, product3.sellingPrice, product3.costPrice, product3.productName, product3.description, product3.barcode, product3.expirationDate, product3.size),
                tuple(product4.id, product4.sellingPrice, product4.costPrice, product4.productName, product4.description, product4.barcode, product4.expirationDate, product4.size),
                tuple(product5.id, product5.sellingPrice, product5.costPrice, product5.productName, product5.description, product5.barcode, product5.expirationDate, product5.size),
                tuple(product6.id, product6.sellingPrice, product6.costPrice, product6.productName, product6.description, product6.barcode, product6.expirationDate, product6.size),
                tuple(product7.id, product7.sellingPrice, product7.costPrice, product7.productName, product7.description, product7.barcode, product7.expirationDate, product7.size),
            )
    }

    @Test
    fun `상품 id 목록으로 상품 목록을 조회한다`() {
        // given
        val category = productCategoryJpaRepository.save(ProductCategory("커피"))
        val product1 = productJpaRepository.save(
            Product(
                category,
                BigDecimal("4000.00"),
                BigDecimal("1000.00"),
                "아메리카노1",
                "고급 원두로 만든 아메리카노1 입니다.",
                "barcode",
                LocalDate.of(2024, 2, 20),
                CafeProductSize.SMALL
            )
        )
        val product2 = productJpaRepository.save(
            Product(
                category,
                BigDecimal("4000.00"),
                BigDecimal("1000.00"),
                "아메리카노2",
                "고급 원두로 만든 아메리카노2 입니다.",
                "barcode",
                LocalDate.of(2024, 2, 20),
                CafeProductSize.SMALL
            )
        )
        val product3 = productJpaRepository.save(
            Product(
                category,
                BigDecimal("4000.00"),
                BigDecimal("1000.00"),
                "아메리카노3",
                "고급 원두로 만든 아메리카노3 입니다.",
                "barcode",
                LocalDate.of(2024, 2, 20),
                CafeProductSize.SMALL
            )
        )
        val product4 = productJpaRepository.save(
            Product(
                category,
                BigDecimal("4000.00"),
                BigDecimal("1000.00"),
                "아메리카노4",
                "고급 원두로 만든 아메리카노4 입니다.",
                "barcode",
                LocalDate.of(2024, 2, 20),
                CafeProductSize.SMALL
            )
        )
        val product5 = productJpaRepository.save(
            Product(
                category,
                BigDecimal("4000.00"),
                BigDecimal("1000.00"),
                "아메리카노5",
                "고급 원두로 만든 아메리카노5 입니다.",
                "barcode",
                LocalDate.of(2024, 2, 20),
                CafeProductSize.SMALL
            )
        )
        val product6 = productJpaRepository.save(
            Product(
                category,
                BigDecimal("4000.00"),
                BigDecimal("1000.00"),
                "아메리카노6",
                "고급 원두로 만든 아메리카노6 입니다.",
                "barcode",
                LocalDate.of(2024, 2, 20),
                CafeProductSize.SMALL
            )
        )
        val product7 = productJpaRepository.save(
            Product(
                category,
                BigDecimal("4000.00"),
                BigDecimal("1000.00"),
                "아메리카노7",
                "고급 원두로 만든 아메리카노7 입니다.",
                "barcode",
                LocalDate.of(2024, 2, 20),
                CafeProductSize.SMALL
            )
        )
        val product8 = productJpaRepository.save(
            Product(
                category,
                BigDecimal("4000.00"),
                BigDecimal("1000.00"),
                "카페 라떼1",
                "고급 원두로 만든 카페 라떼1 입니다.",
                "barcode",
                LocalDate.of(2024, 2, 20),
                CafeProductSize.SMALL
            )
        )
        val product9 = productJpaRepository.save(
            Product(
                category,
                BigDecimal("4000.00"),
                BigDecimal("1000.00"),
                "카페 라떼2",
                "고급 원두로 만든 카페 라떼2 입니다.",
                "barcode",
                LocalDate.of(2024, 2, 20),
                CafeProductSize.SMALL
            )
        )
        val product10 = productJpaRepository.save(
            Product(
                category,
                BigDecimal("4000.00"),
                BigDecimal("1000.00"),
                "카페 라떼3",
                "고급 원두로 만든 카페 라떼3 입니다.",
                "barcode",
                LocalDate.of(2024, 2, 20),
                CafeProductSize.SMALL
            )
        )

        // when
        val result = productQueryRepository.findAllByIds(listOf(product1.id, product2.id, product3.id, product4.id, product5.id, product6.id, product7.id))

        // then
        assertThat(result).hasSize(7)
            .extracting("id", "sellingPrice", "costPrice", "productName", "description", "barcode", "expirationDate", "size")
            .containsExactlyInAnyOrder(
                tuple(product1.id, product1.sellingPrice, product1.costPrice, product1.productName, product1.description, product1.barcode, product1.expirationDate, product1.size),
                tuple(product2.id, product2.sellingPrice, product2.costPrice, product2.productName, product2.description, product2.barcode, product2.expirationDate, product2.size),
                tuple(product3.id, product3.sellingPrice, product3.costPrice, product3.productName, product3.description, product3.barcode, product3.expirationDate, product3.size),
                tuple(product4.id, product4.sellingPrice, product4.costPrice, product4.productName, product4.description, product4.barcode, product4.expirationDate, product4.size),
                tuple(product5.id, product5.sellingPrice, product5.costPrice, product5.productName, product5.description, product5.barcode, product5.expirationDate, product5.size),
                tuple(product6.id, product6.sellingPrice, product6.costPrice, product6.productName, product6.description, product6.barcode, product6.expirationDate, product6.size),
                tuple(product7.id, product7.sellingPrice, product7.costPrice, product7.productName, product7.description, product7.barcode, product7.expirationDate, product7.size),
            )
    }
}