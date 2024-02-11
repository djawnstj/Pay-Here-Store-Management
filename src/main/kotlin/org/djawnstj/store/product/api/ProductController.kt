package org.djawnstj.store.product.api

import jakarta.validation.Valid
import org.djawnstj.store.common.api.Response
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
import org.djawnstj.store.product.service.ProductService
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
class ProductController(
    private val productService: ProductService
) {

    @PostMapping("/api/v1/product-categories")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun registerCategory(@Valid @RequestBody request: ProductCategoryRegistrationRequest) =
        productService.registerProductCategory(request)

    @PostMapping("/api/v1/products")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun register(@Valid @RequestBody request: ProductRegistrationRequest) =
        productService.registerProduct(request)

    @PatchMapping("/api/v1/products")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun update(@Valid @RequestBody request: ProductUpdateRequest) =
        productService.updateProduct(request)

    @DeleteMapping("/api/v1/products")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(@Valid @RequestBody request: ProductDeleteRequest) =
        productService.deleteProduct(request)

    @GetMapping("/api/v1/products")
    fun getListByPage(pageable: Pageable): Response<ProductListResponse> =
        Response.success(productService.getProductsByPage(ProductListRequest(pageable)))

    @GetMapping("/api/v1/products/{id}")
    fun getDetail(@PathVariable(name = "id") id: Long): Response<ProductDetailResponse> =
        Response.success(productService.getProductDetail(ProductDetailRequest(id)))

    @GetMapping("/api/v1/products/search")
    fun search(@Valid request: ProductSearchRequest): Response<ProductSearchResponse> =
        Response.success(productService.searchProducts(request))

}