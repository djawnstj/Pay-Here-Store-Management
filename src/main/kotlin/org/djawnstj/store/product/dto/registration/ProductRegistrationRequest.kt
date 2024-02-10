package org.djawnstj.store.product.dto.registration

import org.djawnstj.store.product.entity.CafeProductSize
import java.math.BigDecimal
import java.time.LocalDate

data class ProductRegistrationRequest(
    val categoryId: Long?,
    val sellingPrice: BigDecimal?,
    val costPrice: BigDecimal?,
    val productName: String?,
    val description: String?,
    val barcode: String?,
    val expirationDate: LocalDate?,
    val size: CafeProductSize?,
)