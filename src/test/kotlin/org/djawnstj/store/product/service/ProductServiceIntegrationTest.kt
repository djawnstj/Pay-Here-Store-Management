package org.djawnstj.store.product.service

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.tuple
import org.djawnstj.store.IntegrationTestSupport
import org.djawnstj.store.product.dto.categoryregistration.ProductCategoryRegistrationRequest
import org.djawnstj.store.product.dto.delete.ProductDeleteRequest
import org.djawnstj.store.product.dto.detail.ProductDetailRequest
import org.djawnstj.store.product.dto.registration.ProductRegistrationRequest
import org.djawnstj.store.product.dto.search.ProductSearchRequest
import org.djawnstj.store.product.dto.update.ProductUpdateRequest
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
import org.springframework.data.domain.PageRequest
import org.springframework.data.repository.findByIdOrNull
import java.math.BigDecimal
import java.time.LocalDate

class ProductServiceIntegrationTest: IntegrationTestSupport() {

    @Autowired
    private lateinit var productService: ProductService

    @Autowired
    private lateinit var productJpaRepository: ProductJpaRepository
    @Autowired
    private lateinit var productCategoryJpaRepository: ProductCategoryJpaRepository
    @Autowired
    private lateinit var productNameQueryRepository: ProductNameQueryRepository

    @AfterEach
    fun tearDown() {
        productJpaRepository.deleteAll()
        productCategoryJpaRepository.deleteAll()
        productNameQueryRepository.deleteAll()
    }

    @Test
    fun `상품 카테고리를 등록한다`() {
        // given
        val request = ProductCategoryRegistrationRequest("커피")

        // when
        productService.registerProductCategory(request)

        // then
        val result = productCategoryJpaRepository.findAll()
        assertThat(result).hasSize(1)
        assertThat(result[0].categoryName).isEqualTo(request.categoryName)
    }

    @Test
    fun `상품을 등록한다`() {
        // given
        val productCategory = productCategoryJpaRepository.save(ProductCategory("커피"))
        val request = ProductRegistrationRequest(
            productCategory.id,
            BigDecimal("4000.00"),
            BigDecimal("1000.00"),
            "아메리카노",
            "고급 원두로 만든 아메리카노 입니다.",
            "barcode",
            LocalDate.of(2024, 2, 20),
            CafeProductSize.SMALL
        )

        // when
        productService.registerProduct(request)

        // then
        val result = productJpaRepository.findAll()
        assertThat(result).hasSize(1)
            .extracting("sellingPrice", "costPrice", "productName", "description", "barcode", "expirationDate", "size")
            .containsExactlyInAnyOrder(
                tuple(request.sellingPrice, request.costPrice, request.productName, request.description, request.barcode, request.expirationDate, request.size)
            )
    }

    @Test
    fun `상품을 수정한다`() {
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
        val request = ProductUpdateRequest(
            product.id,
            category.id,
            costPrice = BigDecimal("1500.00"),
            size = CafeProductSize.LARGE
        )

        // when
        productService.updateProduct(request)

        // then
        val result = productJpaRepository.findByIdOrNull(product.id)
        assertThat(result)
            .extracting("sellingPrice", "costPrice", "productName", "description", "barcode", "expirationDate", "size")
            .contains(product.sellingPrice, request.costPrice, product.productName, product.description, product.barcode, product.expirationDate, request.size)
    }

    @Test
    fun `등록된 상품을 삭제한다`() {
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
        val request = ProductDeleteRequest(product.id)

        // when
        productService.deleteProduct(request)

        // then
        val result = productJpaRepository.findByIdOrNull(product.id)
        assertThat(result?.deletedAt).isNotNull()
    }

    @Test
    fun `상품을 페이지 단위로 조회한다`() {
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
        val pageable = PageRequest.of(1, 5)

        // when
        val result = productService.getProductsByPage(pageable)

        // then
        assertThat(result.products).hasSize(5)
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
    fun `상품을 상세 조회한다`() {
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
        val request = ProductDetailRequest(product.id)

        // when
        val result = productService.getProductDetail(request)

        // then
        assertThat(result.product)
            .extracting("id", "sellingPrice", "costPrice", "productName", "description", "barcode", "expirationDate", "size")
            .contains(product.sellingPrice, product.costPrice, product.productName, product.description, product.barcode, product.expirationDate, product.size)
    }

    @Test
    fun `상품 이름을 like 조건으로 검색한다`() {
        // given
        val category = productCategoryJpaRepository.save(ProductCategory("커피"))
        val product1 = Product(
            category,
            BigDecimal("4000.00"),
            BigDecimal("1000.00"),
            "슈크림 라떼",
            "슈크림이 올라간 라떼 입니다.",
            "barcode",
            LocalDate.of(2024, 2, 20),
            CafeProductSize.SMALL
        )
        val product2 = Product(
            category,
            BigDecimal("4000.00"),
            BigDecimal("1000.00"),
            "휘핑크림 라떼",
            "휘핑크림이 올라간 라떼 입니다.",
            "barcode",
            LocalDate.of(2024, 2, 20),
            CafeProductSize.SMALL
        )
        val product3 = Product(
            category,
            BigDecimal("4000.00"),
            BigDecimal("1000.00"),
            "아메리카노",
            "고급 원두로 만든 아메리카노 입니다.",
            "barcode",
            LocalDate.of(2024, 2, 20),
            CafeProductSize.SMALL
        )
        productJpaRepository.saveAll(listOf(product1, product2, product3,))

        // when
        val result = productService.searchProducts(ProductSearchRequest("크림"))

        // then
        assertThat(result.products).hasSize(2)
            .extracting("id", "sellingPrice", "costPrice", "productName", "description", "barcode", "expirationDate", "size")
            .containsExactlyInAnyOrder(
                tuple(product1.id, product1.sellingPrice, product1.costPrice, product1.productName, product1.description, product1.barcode, product1.expirationDate, product1.size),
                tuple(product2.id, product2.sellingPrice, product2.costPrice, product2.productName, product2.description, product2.barcode, product2.expirationDate, product2.size),
            )
    }

    @Test
    fun `상품 이름을 한글 초성으로 검색한다`() {
        // given
        val category = productCategoryJpaRepository.save(ProductCategory("커피"))
        val product1 = productJpaRepository.save(Product(
            category,
            BigDecimal("4000.00"),
            BigDecimal("1000.00"),
            "슈크림 라떼",
            "슈크림이 올라간 라떼 입니다.",
            "barcode",
            LocalDate.of(2024, 2, 20),
            CafeProductSize.SMALL
        ))
        val product2 = productJpaRepository.save(Product(
            category,
            BigDecimal("4000.00"),
            BigDecimal("1000.00"),
            "휘핑크림 라떼",
            "휘핑크림이 올라간 라떼 입니다.",
            "barcode",
            LocalDate.of(2024, 2, 20),
            CafeProductSize.SMALL
        ))
        val product3 = productJpaRepository.save(Product(
            category,
            BigDecimal("4000.00"),
            BigDecimal("1000.00"),
            "아메리카노",
            "고급 원두로 만든 아메리카노 입니다.",
            "barcode",
            LocalDate.of(2024, 2, 20),
            CafeProductSize.SMALL
        ))

        productNameQueryRepository.saveAll(listOf(ProductNameQuery(product1.id, "ㅅㅋㄹ ㄹㄸ"), ProductNameQuery(product2.id, "ㅎㅍㅋㄹ ㄹㄸ"), ProductNameQuery(product3.id, "ㅇㅁㄹㅋㄴ")))

        // when
        val result = productService.searchProducts(ProductSearchRequest("ㅋㄹ"))

        // then
        assertThat(result.products).hasSize(2)
            .extracting("id", "sellingPrice", "costPrice", "productName", "description", "barcode", "expirationDate", "size")
            .containsExactlyInAnyOrder(
                tuple(product1.id, product1.sellingPrice, product1.costPrice, product1.productName, product1.description, product1.barcode, product1.expirationDate, product1.size),
                tuple(product2.id, product2.sellingPrice, product2.costPrice, product2.productName, product2.description, product2.barcode, product2.expirationDate, product2.size),
            )
    }

}