package org.djawnstj.store.product.dto.list

import org.springframework.data.domain.Pageable

data class ProductListRequest(
    val pageable: Pageable
)