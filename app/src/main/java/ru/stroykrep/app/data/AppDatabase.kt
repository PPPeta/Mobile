package ru.stroykrep.app.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ru.stroykrep.app.data.dao.CartDao
import ru.stroykrep.app.data.dao.CategoryDao
import ru.stroykrep.app.data.dao.FavoriteDao
import ru.stroykrep.app.data.dao.OrderDao
import ru.stroykrep.app.data.dao.ProductDao
import ru.stroykrep.app.data.dao.UserDao
import ru.stroykrep.app.data.entity.CartItem
import ru.stroykrep.app.data.entity.Category
import ru.stroykrep.app.data.entity.FavoriteItem
import ru.stroykrep.app.data.entity.Order
import ru.stroykrep.app.data.entity.OrderItem
import ru.stroykrep.app.data.entity.Product
import ru.stroykrep.app.data.entity.User

@Database(
    entities = [
        User::class,
        Category::class,
        Product::class,
        CartItem::class,
        FavoriteItem::class,
        Order::class,
        OrderItem::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun categoryDao(): CategoryDao
    abstract fun productDao(): ProductDao
    abstract fun cartDao(): CartDao
    abstract fun favoriteDao(): FavoriteDao
    abstract fun orderDao(): OrderDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "stroykrep.db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}
