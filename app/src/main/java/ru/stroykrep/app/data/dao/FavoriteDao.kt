package ru.stroykrep.app.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.stroykrep.app.data.entity.FavoriteItem
import ru.stroykrep.app.data.entity.Product

@Dao
interface FavoriteDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun add(item: FavoriteItem)

    @Query("DELETE FROM favorites WHERE userId = :userId AND productId = :productId")
    suspend fun remove(userId: Long, productId: Long)

    @Query("SELECT EXISTS(SELECT 1 FROM favorites WHERE userId = :userId AND productId = :productId)")
    fun observeIsFavorite(userId: Long, productId: Long): Flow<Boolean>

    @Query(
        "SELECT p.* FROM favorites f INNER JOIN products p ON p.id = f.productId " +
            "WHERE f.userId = :userId ORDER BY p.name"
    )
    fun observeFavorites(userId: Long): Flow<List<Product>>
}
