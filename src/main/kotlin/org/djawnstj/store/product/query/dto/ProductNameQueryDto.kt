package org.djawnstj.store.product.query.dto

import org.djawnstj.store.product.query.entity.ProductNameQuery

data class ProductNameQueryDto(
    val id: Long,
    val nameQuery: String
) {
    companion object {
        fun of(query: ProductNameQuery): ProductNameQueryDto = ProductNameQueryDto(query.id, query.nameQuery)
    }
}