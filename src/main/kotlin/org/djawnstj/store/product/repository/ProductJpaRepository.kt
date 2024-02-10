package org.djawnstj.store.product.repository

import com.linecorp.kotlinjdsl.support.spring.data.jpa.repository.KotlinJdslJpqlExecutor
import org.djawnstj.store.product.entity.Product
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ProductJpaRepository: JpaRepository<Product, Long>, KotlinJdslJpqlExecutor {
}