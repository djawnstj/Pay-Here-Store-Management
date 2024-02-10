package org.djawnstj.store.product.repository

import org.djawnstj.store.product.entity.ProductCategory
import org.springframework.stereotype.Repository

@Repository
class ProductCategoryQueryRepository(
    private val productCategoryJpaRepository: ProductCategoryJpaRepository
) {

    fun findByName(name: String): ProductCategory? = productCategoryJpaRepository.findAll {
        select(
            entity(ProductCategory::class)
        ).from(
            entity(ProductCategory::class)
        ).where(
            path(ProductCategory::deletedAt).isNull()
                .and(
                    path(ProductCategory::categoryName).equal(name)
                )
        )
    }.firstOrNull()

    fun findById(id: Long): ProductCategory? = productCategoryJpaRepository.findAll {
        select(
            entity(ProductCategory::class)
        ).from(
            entity(ProductCategory::class)
        ).where(
            path(ProductCategory::deletedAt).isNull()
                .and(
                    path(ProductCategory::id).equal(id)
                )
        )
    }.firstOrNull()

}