package org.djawnstj.store.product.entity

import jakarta.persistence.*
import org.djawnstj.store.common.entity.BaseEntity
import org.djawnstj.store.product.dto.update.ProductUpdateRequest
import java.math.BigDecimal
import java.time.LocalDate

@Entity
@Table(
    name = "product",
)
class Product(
    @field:ManyToOne(fetch = FetchType.LAZY)
    @field:JoinColumn(name = "category_id")
    var category: ProductCategory,
    @field:Column(name = "selling_price", precision = 10, scale = 2)
    var sellingPrice: BigDecimal,
    @field:Column(name = "cost_price", precision = 10, scale = 2)
    var costPrice: BigDecimal,
    @field:Column(name = "product_name")
    var productName: String,
    @field:Column(name = "description")
    @field:Lob
    var description: String,
    @field:Column(name = "barcode")
    var barcode: String,
    @field:Column(name = "expiration_date")
    var expirationDate: LocalDate,
    @field:Column(name = "size")
    @field:Enumerated(EnumType.STRING)
    var size: CafeProductSize,
): BaseEntity() {

    fun update(
        category: ProductCategory? = null,
        sellingPrice: BigDecimal? = null,
        costPrice: BigDecimal? = null,
        productName: String? = null,
        description: String? = null,
        barcode: String? = null,
        expirationDate: LocalDate? = null,
        size: CafeProductSize? = null,
    ) {
        category?.let { this.category = category }
        sellingPrice?.let { this.sellingPrice = sellingPrice }
        costPrice?.let { this.costPrice = costPrice }
        productName?.let { this.productName = productName }
        description?.let { this.description = description }
        barcode?.let { this.barcode = barcode }
        expirationDate?.let { this.expirationDate = expirationDate }
        size?.let { this.size = size }
    }

    fun update(category: ProductCategory? = null, request: ProductUpdateRequest) {
        this.update(category, request.sellingPrice, request.costPrice, request.productName, request.description, request.barcode, request.expirationDate, request.size, )
    }
}