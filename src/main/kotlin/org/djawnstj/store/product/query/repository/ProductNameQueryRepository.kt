package org.djawnstj.store.product.query.repository

import org.djawnstj.store.product.query.entity.ProductNameQuery
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository
import org.springframework.stereotype.Repository

@Repository
interface ProductNameQueryRepository: ElasticsearchRepository<ProductNameQuery, Long> {

    fun findByNameQueryContaining(nameQuery: String): List<ProductNameQuery>

}