package ru.stroykrep.app.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow
import ru.stroykrep.app.data.entity.Order
import ru.stroykrep.app.data.entity.OrderItem
import ru.stroykrep.app.data.model.OrderWithItems

@Dao
interface OrderDao {

    @Insert
    suspend fun insertOrder(order: Order): Long

    @Insert
    suspend fun insertItems(items: List<OrderItem>)

    @Transaction
    @Query("SELECT * FROM orders WHERE userId = :userId ORDER BY createdAt DESC")
    fun observeUserOrders(userId: Long): Flow<List<OrderWithItems>>

    @Transaction
    @Query("SELECT * FROM orders WHERE id = :orderId LIMIT 1")
    suspend fun getOrder(orderId: Long): OrderWithItems?

    @Query("UPDATE orders SET status = :status WHERE id = :orderId")
    suspend fun updateStatus(orderId: Long, status: String)
}
