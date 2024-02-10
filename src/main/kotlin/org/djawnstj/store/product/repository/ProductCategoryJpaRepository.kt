package org.djawnstj.store.product.repository

import com.linecorp.kotlinjdsl.support.spring.data.jpa.repository.KotlinJdslJpqlExecutor
import org.djawnstj.store.product.entity.ProductCategory
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ProductCategoryJpaRepository: JpaRepository<ProductCategory, Long>, KotlinJdslJpqlExecutor {
}