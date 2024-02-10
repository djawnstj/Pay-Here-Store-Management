package org.djawnstj.store.product.dto

import org.djawnstj.store.product.entity.CafeProductSize
import org.djawnstj.store.product.entity.Product
import java.math.BigDecimal
import java.time.LocalDate

data class ProductDto(
    val id: Long = 0,
    val categoryDto: ProductCategoryDto,
    val sellingPrice: BigDecimal,
    val costPrice: BigDecimal,
    val productName: String,
    val description: String,
    val barcode: String,
    val expirationDate: LocalDate,
    val size: CafeProductSize,
) {
    companion object {
        fun of(product: Product): ProductDto = ProductDto(
            product.id,
            ProductCategoryDto.of(product.category),
            product.sellingPrice,
            product.costPrice,
            product.productName,
            product.description,
            product.barcode,
            product.expirationDate,
            product.size,)
    }
}
