package org.djawnstj.store.product.query.service

import org.djawnstj.store.product.dto.ProductDto
import org.djawnstj.store.product.query.dto.ProductNameQueryDto
import org.djawnstj.store.product.query.entity.ProductNameQuery
import org.djawnstj.store.product.query.repository.ProductNameQueryRepository
import org.djawnstj.store.product.query.util.ProductNameQueryUtil
import org.springframework.stereotype.Service

@Service
class ProductNameQueryService(
    private val productNameQueryRepository: ProductNameQueryRepository,
    private val productNameQueryUtil: ProductNameQueryUtil,
) {

    fun saveQuery(product: ProductDto) {
        if (!productNameQueryUtil.hasKoreanInitials(product.productName)) return

        val query = productNameQueryUtil.extractKoreanInitialsQuery(product.productName)
        productNameQueryRepository.save(ProductNameQuery(product.id, query))
    }

    fun searchByKoreanInitialsQuery(query: String): List<ProductNameQueryDto> =
        if (productNameQueryUtil.isOnlyKoreanInitials(query))
            productNameQueryRepository.findByNameQueryContaining(query).map(ProductNameQueryDto::of)
        else emptyList()

}