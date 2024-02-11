package org.djawnstj.store.product.service

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.djawnstj.store.common.exception.ErrorCode
import org.djawnstj.store.common.exception.GlobalException
import org.djawnstj.store.product.dto.ProductDto
import org.djawnstj.store.product.dto.categoryregistration.ProductCategoryRegistrationRequest
import org.djawnstj.store.product.dto.delete.ProductDeleteRequest
import org.djawnstj.store.product.dto.detail.ProductDetailRequest
import org.djawnstj.store.product.dto.list.ProductListRequest
import org.djawnstj.store.product.dto.registration.ProductRegistrationRequest
import org.djawnstj.store.product.dto.search.ProductSearchRequest
import org.djawnstj.store.product.dto.update.ProductUpdateRequest
import org.djawnstj.store.product.entity.Product
import org.djawnstj.store.product.entity.ProductCategory
import org.djawnstj.store.product.query.dto.ProductNameQueryDto
import org.djawnstj.store.product.query.service.ProductNameQueryService
import org.djawnstj.store.product.repository.ProductCategoryJpaRepository
import org.djawnstj.store.product.repository.ProductCategoryQueryRepository
import org.djawnstj.store.product.repository.ProductJpaRepository
import org.djawnstj.store.product.repository.ProductQueryRepository
import org.junit.jupiter.api.Test
import org.springframework.data.domain.Pageable

class ProductServiceTest {

    private val productJpaRepository: ProductJpaRepository = mockk()
    private val productQueryRepository: ProductQueryRepository = mockk()
    private val productNameQueryService: ProductNameQueryService = mockk(relaxed = true)
    private val productCategoryJpaRepository: ProductCategoryJpaRepository = mockk()
    private val productCategoryQueryRepository: ProductCategoryQueryRepository = mockk()

    private val productService = ProductService(productJpaRepository, productQueryRepository,
        productNameQueryService, productCategoryJpaRepository, productCategoryQueryRepository
    )

    @Test
    fun `상품 카테고리를 등록한다`() {
        // given
        val request: ProductCategoryRegistrationRequest = mockk(relaxed = true)

        every { productCategoryQueryRepository.findByName(request.categoryName!!) } returns null
        every { productCategoryJpaRepository.save(any<ProductCategory>()) } answers { it.invocation.args[0] as ProductCategory }

        // when
        productService.registerProductCategory(request)

        // then
        verify(exactly = 1) { productCategoryJpaRepository.save(any<ProductCategory>()) }
    }

    @Test
    fun `등록하려는 상품 카테고리 명이 이미 존재하는 경우 예외를 반환한다`() {
        // given
        val request: ProductCategoryRegistrationRequest = mockk(relaxed = true)

        every { productCategoryQueryRepository.findByName(request.categoryName!!) } returns mockk()

        // when then
        every { productCategoryJpaRepository.save(any<ProductCategory>()) } answers { it.invocation.args[0] as ProductCategory }
        assertThatThrownBy { productService.registerProductCategory(request) }
            .isInstanceOf(GlobalException::class.java)
            .hasMessage(ErrorCode.DUPLICATED_REGISTER_PRODUCT_CATEGORY_NAME.message)
    }

    @Test
    fun `상품을 등록한다`() {
        // given
        val request: ProductRegistrationRequest = mockk(relaxed = true)

        every { productCategoryQueryRepository.findById(request.categoryId!!) } returns mockk()
        every { productJpaRepository.save(any<Product>()) } returns mockk(relaxed = true)

        // when
        productService.registerProduct(request)

        // then
        verify(exactly = 1) { productJpaRepository.save(any<Product>()) }
        verify(exactly = 1) { productNameQueryService.saveQuery(any<ProductDto>()) }
    }

    @Test
    fun `상품을 등록할 때, 올바른 카테고리를 입력하지 않으면 예외를 반환한다`() {
        // given
        val request: ProductRegistrationRequest = mockk(relaxed = true)

        every { productCategoryQueryRepository.findById(request.categoryId!!) } returns null

        // when then
        assertThatThrownBy { productService.registerProduct(request) }
            .isInstanceOf(GlobalException::class.java)
            .hasMessage(ErrorCode.PRODUCT_CATEGORY_NOT_FOUND.message)
    }

    @Test
    fun `상품을 수정한다`() {
        // given
        val request: ProductUpdateRequest = mockk(relaxed = true)
        val product: Product = mockk(relaxed = true)
        val productCategory: ProductCategory = mockk(relaxed = true)

        every { productQueryRepository.findById(request.id!!) } returns product
        every { productCategoryQueryRepository.findById(request.categoryId!!) } returns productCategory
        every { productJpaRepository.save(product) } returns product

        // when
        productService.updateProduct(request)

        // then
        verify(exactly = 1) { productJpaRepository.save(product) }
        verify(exactly = 1) { productNameQueryService.saveQuery(any<ProductDto>()) }
    }

    @Test
    fun `상품 수정 요청을 받았을 때, 상품을 찾지 못하면 예외를 반환한다`() {
        // given
        val request: ProductUpdateRequest = mockk(relaxed = true)

        every { productQueryRepository.findById(request.id!!) } returns null

        // when then
        assertThatThrownBy { productService.updateProduct(request) }
            .isInstanceOf(GlobalException::class.java)
            .hasMessage(ErrorCode.PRODUCT_NOT_FOUND.message)
    }

    @Test
    fun `상품 수정 요청을 받았을 때, 상품 카테고리를 찾지 못하면 예외를 반환한다`() {
        // given
        val request: ProductUpdateRequest = mockk(relaxed = true)
        val product: Product = mockk(relaxed = true)

        every { productQueryRepository.findById(request.id!!) } returns product
        every { productCategoryQueryRepository.findById(request.categoryId!!) } returns null

        // when then
        assertThatThrownBy { productService.updateProduct(request) }
            .isInstanceOf(GlobalException::class.java)
            .hasMessage(ErrorCode.PRODUCT_CATEGORY_NOT_FOUND.message)
    }

    @Test
    fun `상품을 삭제한다`() {
        // given
        val request: ProductDeleteRequest = mockk(relaxed = true)
        val product: Product = mockk(relaxed = true)

        every { productQueryRepository.findById(request.id!!) } returns product
        every { productJpaRepository.save(product) } returns mockk()

        // when
        productService.deleteProduct(request)

        // then
        verify(exactly = 1) { product.delete() }
        verify(exactly = 1) { productJpaRepository.save(product) }
    }

    @Test
    fun `상품 삭제 요청을 받았을 때, 상품을 찾지 못하면 예외를 반환한다`() {
        // given
        val request: ProductDeleteRequest = mockk(relaxed = true)

        every { productQueryRepository.findById(request.id!!) } returns null

        // when then
        assertThatThrownBy { productService.deleteProduct(request) }
            .isInstanceOf(GlobalException::class.java)
            .hasMessage(ErrorCode.PRODUCT_NOT_FOUND.message)
    }

    @Test
    fun `페이지 단위로 상품 목록을 조회한다`() {
        // given
        val pageable: Pageable = mockk(relaxed = true)
        val products: List<Product> = mockk(relaxed = true)

        every { productQueryRepository.findAll(pageable) } returns products

        // when then
        productService.getProductsByPage(ProductListRequest(pageable))
    }

    @Test
    fun `상품을 상세 조회한다`() {
        // given
        val request: ProductDetailRequest = mockk(relaxed = true)
        val product: Product = mockk(relaxed = true)

        every { productQueryRepository.findById(request.id!!) } returns product

        // when then
        productService.getProductDetail(request)
    }

    @Test
    fun `상품을 상세 조회할 때, 상품을 찾지 못하면 예외를 반환한다`() {
        // given
        val request: ProductDetailRequest = mockk(relaxed = true)

        every { productQueryRepository.findById(request.id!!) } returns null

        // when then
        assertThatThrownBy { productService.getProductDetail(request) }
            .isInstanceOf(GlobalException::class.java)
            .hasMessage(ErrorCode.PRODUCT_NOT_FOUND.message)
    }

    @Test
    fun `상품 이름을 like 조건으로 상품 목록을 조회를 한다`() {
        // given
        val request: ProductSearchRequest = mockk(relaxed = true)

        every { productNameQueryService.searchByKoreanInitialsQuery(request.query!!) } returns emptyList()
        every { productQueryRepository.findAllByNameLike(request.query!!) } returns mockk(relaxed = true)

        // when
        productService.searchProducts(request)

        // then
        verify(exactly = 1) { productQueryRepository.findAllByNameLike(any<String>()) }
    }

    @Test
    fun `상품 이름을 한글 초성 검색어로 상품 목록을 조회를 한다`() {
        // given
        val request: ProductSearchRequest = mockk(relaxed = true)
        val queryDtos: List<ProductNameQueryDto> = mockk(relaxed = true)

        every { productNameQueryService.searchByKoreanInitialsQuery(request.query!!) } returns queryDtos
        every { productQueryRepository.findAllByIds(any<List<Long>>()) } returns mockk(relaxed = true)

        // when
        productService.searchProducts(request)

        // then
        verify(exactly = 1) { productQueryRepository.findAllByIds(any<List<Long>>()) }
    }

}