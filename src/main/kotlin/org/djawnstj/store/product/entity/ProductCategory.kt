package org.djawnstj.store.product.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint
import org.djawnstj.store.common.entity.BaseEntity

@Entity
@Table(
    name = "product_category",
    uniqueConstraints = [UniqueConstraint(name = "uk_product_category_category_name", columnNames = ["category_name"])]
)
class ProductCategory(
    @field:Column(name = "category_name")
    val categoryName: String,
): BaseEntity() {
}