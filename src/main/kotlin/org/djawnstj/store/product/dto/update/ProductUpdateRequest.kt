package org.djawnstj.store.product.dto.update

import org.djawnstj.store.product.entity.CafeProductSize
import java.math.BigDecimal
import java.time.LocalDate

data class ProductUpdateRequest(
    val id: Long?,
    val categoryId: Long?,
    val sellingPrice: BigDecimal? = null,
    val costPrice: BigDecimal? = null,
    val productName: String? = null,
    val description: String? = null,
    val barcode: String? = null,
    val expirationDate: LocalDate? = null,
    val size: CafeProductSize? = null,
)
