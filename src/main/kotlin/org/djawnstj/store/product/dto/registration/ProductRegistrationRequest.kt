package org.djawnstj.store.product.dto.registration

import jakarta.validation.constraints.DecimalMax
import jakarta.validation.constraints.DecimalMin
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import org.djawnstj.store.common.validation.ValidEnum
import org.djawnstj.store.product.entity.CafeProductSize
import java.math.BigDecimal
import java.time.LocalDate

data class ProductRegistrationRequest(
    @field:NotNull(message = "카테고리를 선택해주세요.")
    val categoryId: Long?,
    @field:NotNull(message = "상품 가격을 입력해주세요.")
    @field:DecimalMax(value = "9999999999", message = "상품 가격은 0 ~ 9,999,999,999 까지 입력해주세요.")
    @field:DecimalMin(value = "0", message = "상품 가격은 0 ~ 9,999,999,999 까지 입력해주세요.")
    val sellingPrice: BigDecimal?,
    @field:NotNull(message = "원가를 입력해주세요.")
    @field:DecimalMax(value = "9999999999", message = "원가는 0 ~ 9,999,999,999 까지 입력해주세요.")
    @field:DecimalMin(value = "0", message = "원가는 0 ~ 9,999,999,999 까지 입력해주세요.")
    val costPrice: BigDecimal?,
    @field:NotBlank(message = "상품 이름을 입력해주세요.")
    val productName: String?,
    @field:NotBlank(message = "상품 설명을 입력해주세요.")
    val description: String?,
    @field:NotBlank(message = "바코드를 입력해주세요.")
    val barcode: String?,
    @field:NotNull(message = "유통기한을 입력해주세요.")
    val expirationDate: LocalDate?,
    @field:ValidEnum(enumClass = CafeProductSize::class, message = "잘못된 상품 사이즈 입니다.")
    val size: CafeProductSize?,
)