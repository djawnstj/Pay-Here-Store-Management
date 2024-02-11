package org.djawnstj.store.product.service

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.djawnstj.store.common.exception.ErrorCode
import org.djawnstj.store.common.exception.GlobalException
import org.djawnstj.store.product.dto.ProductDto
import org.djawnstj.store.product.dto.categoryregistration.ProductCategoryRegistrationRequest
import org.djawnstj.store.product.dto.delete.ProductDeleteRequest
import org.djawnstj.store.product.dto.detail.ProductDetailRequest
import org.djawnstj.store.product.dto.detail.ProductDetailResponse
import org.djawnstj.store.product.dto.list.ProductListRequest
import org.djawnstj.store.product.dto.list.ProductListResponse
import org.djawnstj.store.product.dto.registration.ProductRegistrationRequest
import org.djawnstj.store.product.dto.search.ProductSearchRequest
import org.djawnstj.store.product.dto.search.ProductSearchResponse
import org.djawnstj.store.product.dto.update.ProductUpdateRequest
import org.djawnstj.store.product.entity.Product
import org.djawnstj.store.product.entity.ProductCategory
import org.djawnstj.store.product.query.service.ProductNameQueryService
import org.djawnstj.store.product.repository.ProductCategoryJpaRepository
import org.djawnstj.store.product.repository.ProductCategoryQueryRepository
import org.djawnstj.store.product.repository.ProductJpaRepository
import org.djawnstj.store.product.repository.ProductQueryRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class ProductService(
    private val productJpaRepository: ProductJpaRepository,
    private val productQueryRepository: ProductQueryRepository,
    private val productNameQueryService: ProductNameQueryService,
    private val productCategoryJpaRepository: ProductCategoryJpaRepository,
    private val productCategoryQueryRepository: ProductCategoryQueryRepository,
) {

    @Transactional
    fun registerProductCategory(request: ProductCategoryRegistrationRequest) {
        validateDuplicatedName(request.categoryName!!)
        val category = ProductCategory(request.categoryName)
        productCategoryJpaRepository.save(category)
    }

    private fun validateDuplicatedName(name: String) {
        val foundCategory = productCategoryQueryRepository.findByName(name)
        if (foundCategory != null) throw GlobalException(ErrorCode.DUPLICATED_REGISTER_PRODUCT_CATEGORY_NAME)
    }

    @Transactional
    fun registerProduct(request: ProductRegistrationRequest) {
        val savedProduct = request.let {
            val category = productCategoryQueryRepository.findById(it.categoryId!!).isValidCategory()

            val product = Product(
                category = category,
                sellingPrice = it.sellingPrice!!,
                costPrice = it.costPrice!!,
                productName = it.productName!!,
                description = it.description!!,
                barcode = it.barcode!!,
                expirationDate = it.expirationDate!!,
                size = it.size!!,
            )

            productJpaRepository.save(product)
        }

        saveProductNameQuery(savedProduct)
    }

    @Transactional
    fun updateProduct(request: ProductUpdateRequest) {
        val product = productQueryRepository.findById(request.id!!).isValidProduct()
        val category = productCategoryQueryRepository.findById(request.categoryId!!).isValidCategory()

        product.update(category, request)
        productJpaRepository.save(product)

        saveProductNameQuery(product)
    }

    private fun saveProductNameQuery(product: Product) {
        CoroutineScope(Dispatchers.IO).launch {
            productNameQueryService.saveQuery(ProductDto.of(product))
        }
    }

    @Transactional
    fun deleteProduct(request: ProductDeleteRequest) {
        val product = productQueryRepository.findById(request.id!!).isValidProduct()
        product.delete()

        productJpaRepository.save(product)
    }

    fun getProductsByPage(request: ProductListRequest): ProductListResponse {
        val products = productQueryRepository.findAll(request.pageable)
        return ProductListResponse(products.map(ProductDto::of))
    }

    fun getProductDetail(request: ProductDetailRequest): ProductDetailResponse {
        val product = productQueryRepository.findById(request.id!!).isValidProduct()

        return ProductDetailResponse(ProductDto.of(product))
    }

    fun searchProducts(request: ProductSearchRequest): ProductSearchResponse {
        val queries = productNameQueryService.searchByKoreanInitialsQuery(request.query!!)

        val products =
            if (queries.isEmpty()) productQueryRepository.findAllByNameLike(request.query)
            else productQueryRepository.findAllByIds(queries.map { it.id })

        return ProductSearchResponse(products.map(ProductDto::of))
    }

    private fun ProductCategory?.isValidCategory(): ProductCategory {
        if (this == null)  throw GlobalException(ErrorCode.PRODUCT_CATEGORY_NOT_FOUND)
        return this
    }

    private fun Product?.isValidProduct(): Product {
        if (this == null)  throw GlobalException(ErrorCode.PRODUCT_NOT_FOUND)
        return this
    }

}

