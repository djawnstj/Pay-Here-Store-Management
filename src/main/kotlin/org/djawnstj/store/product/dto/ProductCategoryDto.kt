package org.djawnstj.store.product.dto

import org.djawnstj.store.product.entity.ProductCategory

data class ProductCategoryDto(
    val id: Long = 0,
    val categoryName: String,
) {
    companion object {
        fun of(category: ProductCategory): ProductCategoryDto = ProductCategoryDto(category.id, category.categoryName)
    }

}
