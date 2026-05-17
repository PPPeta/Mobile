package ru.stroykrep.app.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.stroykrep.app.data.entity.Product

@Dao
interface ProductDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(products: List<Product>)

    @Query("SELECT COUNT(*) FROM products")
    suspend fun count(): Int

    @Query("SELECT * FROM products WHERE id = :id LIMIT 1")
    suspend fun findById(id: Long): Product?

    @Query("SELECT * FROM products WHERE categoryId = :categoryId ORDER BY name")
    fun observeByCategory(categoryId: Long): Flow<List<Product>>

    @Query("SELECT * FROM products WHERE isPopular = 1 ORDER BY name LIMIT :limit")
    fun observePopular(limit: Int = 10): Flow<List<Product>>

    @Query("SELECT * FROM products WHERE name LIKE '%' || :query || '%' ORDER BY name LIMIT 50")
    fun search(query: String): Flow<List<Product>>

    @Query("SELECT categoryId, COUNT(*) as cnt FROM products GROUP BY categoryId")
    suspend fun countPerCategory(): List<CategoryCount>
}

data class CategoryCount(
    val categoryId: Long,
    val cnt: Int
)
