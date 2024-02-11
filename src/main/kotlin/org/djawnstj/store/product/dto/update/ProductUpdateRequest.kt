package org.djawnstj.store.product.dto.update

import jakarta.validation.constraints.DecimalMax
import jakarta.validation.constraints.DecimalMin
import jakarta.validation.constraints.NotNull
import org.djawnstj.store.common.validation.ValidEnum
import org.djawnstj.store.product.entity.CafeProductSize
import java.math.BigDecimal
import java.time.LocalDate

data class ProductUpdateRequest(
    @field:NotNull(message = "상품 ID 를 입력해주세요.")
    val id: Long?,
    val categoryId: Long? = null,
    @field:DecimalMax(value = "9999999999", message = "상품 가격은 0 ~ 9,999,999,999 까지 입력해주세요.")
    @field:DecimalMin(value = "0", message = "상품 가격은 0 ~ 9,999,999,999 까지 입력해주세요.")
    val sellingPrice: BigDecimal? = null,
    @field:DecimalMax(value = "9999999999", message = "원가는 0 ~ 9,999,999,999 까지 입력해주세요.")
    @field:DecimalMin(value = "0", message = "원가는 0 ~ 9,999,999,999 까지 입력해주세요.")
    val costPrice: BigDecimal? = null,
    val productName: String? = null,
    val description: String? = null,
    val barcode: String? = null,
    val expirationDate: LocalDate? = null,
    @field:ValidEnum(enumClass = CafeProductSize::class, message = "잘못된 상품 사이즈 입니다.", nullable = true)
    val size: CafeProductSize? = null,
)
