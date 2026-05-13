package ru.stroykrep.app.data.model

import androidx.room.Embedded
import ru.stroykrep.app.data.entity.Product

/**
 * Строка корзины вместе с данными товара (для отображения в списке).
 */
data class CartProduct(
    @Embedded val product: Product,
    val quantity: Int
) {
    val lineTotal: Double get() = product.price * quantity
}
