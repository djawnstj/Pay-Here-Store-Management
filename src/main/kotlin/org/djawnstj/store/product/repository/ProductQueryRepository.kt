package org.djawnstj.store.product.repository

import org.djawnstj.store.product.entity.Product
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository

@Repository
class ProductQueryRepository(
    private val productJpaRepository: ProductJpaRepository
) {

    fun findById(id: Long): Product? = productJpaRepository.findAll {
        select(
            entity(Product::class)
        ).from(
            entity(Product::class)
        ).where(
            path(Product::deletedAt).isNull()
                .and(
                    path(Product::id).equal(id)
                )
        )
    }.firstOrNull()

    fun findAll(pageable: Pageable): List<Product> = productJpaRepository.findAll(pageable) {
        select(
            entity(Product::class)
        ).from(
            entity(Product::class)
        ).where(
            path(Product::deletedAt).isNull()
        )
    }.filterNotNull()

    fun findAllByNameLike(name: String): List<Product> = productJpaRepository.findAll {
        select(
            entity(Product::class)
        ).from(
            entity(Product::class)
        ).where(
            path(Product::deletedAt).isNull()
                .and(
                    path(Product::productName).like("%$name%")
                )
        )
    }.filterNotNull()

    fun findAllByIds(ids: List<Long>): List<Product> = productJpaRepository.findAll {
        select(
            entity(Product::class)
        ).from(
            entity(Product::class)
        ).where(
            path(Product::deletedAt).isNull()
                .and(
                    path(Product::id).`in`(ids)
                )
        )
    }.filterNotNull()

}