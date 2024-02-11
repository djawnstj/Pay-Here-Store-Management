package org.djawnstj.store.product.dto.categoryregistration

import jakarta.validation.constraints.NotBlank

data class ProductCategoryRegistrationRequest(
    @field:NotBlank(message = "상품 카테고리 이름을 입력해 주세요.")
    val categoryName: String?
)