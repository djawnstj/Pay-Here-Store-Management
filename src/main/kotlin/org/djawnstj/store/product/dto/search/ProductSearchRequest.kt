package org.djawnstj.store.product.dto.search

import jakarta.validation.constraints.NotBlank

data class ProductSearchRequest(
    @field:NotBlank(message = "검색어를 입력해주세요.")
    val query: String?
)