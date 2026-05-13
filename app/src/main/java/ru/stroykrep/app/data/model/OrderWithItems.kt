package ru.stroykrep.app.data.model

import androidx.room.Embedded
import androidx.room.Relation
import ru.stroykrep.app.data.entity.Order
import ru.stroykrep.app.data.entity.OrderItem

data class OrderWithItems(
    @Embedded val order: Order,
    @Relation(
        parentColumn = "id",
        entityColumn = "orderId"
    )
    val items: List<OrderItem>
)
