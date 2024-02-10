package org.djawnstj.store.product.query.entity

import org.springframework.data.annotation.Id
import org.springframework.data.elasticsearch.annotations.Document

@Document(indexName = "product_name_query")
data class ProductNameQuery(
    @field:Id
    val id: Long,
    val nameQuery: String
)