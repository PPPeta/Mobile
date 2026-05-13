package ru.stroykrep.app.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.stroykrep.app.data.entity.CartItem
import ru.stroykrep.app.data.model.CartProduct

@Dao
interface CartDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(item: CartItem)

    @Query("SELECT quantity FROM cart_items WHERE userId = :userId AND productId = :productId")
    suspend fun getQuantity(userId: Long, productId: Long): Int?

    @Query("UPDATE cart_items SET quantity = :quantity WHERE userId = :userId AND productId = :productId")
    suspend fun updateQuantity(userId: Long, productId: Long, quantity: Int)

    @Query("DELETE FROM cart_items WHERE userId = :userId AND productId = :productId")
    suspend fun remove(userId: Long, productId: Long)

    @Query("DELETE FROM cart_items WHERE userId = :userId")
    suspend fun clear(userId: Long)

    @Query(
        "SELECT p.*, c.quantity AS quantity " +
            "FROM cart_items c INNER JOIN products p ON p.id = c.productId " +
            "WHERE c.userId = :userId"
    )
    fun observeCart(userId: Long): Flow<List<CartProduct>>

    @Query(
        "SELECT p.*, c.quantity AS quantity " +
            "FROM cart_items c INNER JOIN products p ON p.id = c.productId " +
            "WHERE c.userId = :userId"
    )
    suspend fun getCart(userId: Long): List<CartProduct>

    @Query("SELECT IFNULL(SUM(quantity), 0) FROM cart_items WHERE userId = :userId")
    fun observeCartCount(userId: Long): Flow<Int>
}
