package org.djawnstj.store.product.dto.delete

import jakarta.validation.constraints.NotNull

data class ProductDeleteRequest(
    @field:NotNull(message = "상품 ID 를 입력해주세요.")
    val id: Long?
)
