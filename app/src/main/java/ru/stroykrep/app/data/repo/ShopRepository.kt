package ru.stroykrep.app.data.repo

import kotlinx.coroutines.flow.Flow
import ru.stroykrep.app.data.dao.CartDao
import ru.stroykrep.app.data.dao.CategoryDao
import ru.stroykrep.app.data.dao.FavoriteDao
import ru.stroykrep.app.data.dao.OrderDao
import ru.stroykrep.app.data.dao.ProductDao
import ru.stroykrep.app.data.entity.CartItem
import ru.stroykrep.app.data.entity.Category
import ru.stroykrep.app.data.entity.FavoriteItem
import ru.stroykrep.app.data.entity.Order
import ru.stroykrep.app.data.entity.OrderItem
import ru.stroykrep.app.data.entity.Product
import ru.stroykrep.app.data.model.CartProduct
import ru.stroykrep.app.data.model.OrderWithItems

class ShopRepository(
    private val categoryDao: CategoryDao,
    private val productDao: ProductDao,
    private val cartDao: CartDao,
    private val favoriteDao: FavoriteDao,
    private val orderDao: OrderDao
) {

    // Catalog
    fun observeCategories(): Flow<List<Category>> = categoryDao.observeAll()
    suspend fun getCategory(id: Long): Category? = categoryDao.findById(id)

    fun observeProducts(categoryId: Long): Flow<List<Product>> =
        productDao.observeByCategory(categoryId)

    fun observePopular(limit: Int = 10): Flow<List<Product>> = productDao.observePopular(limit)

    fun searchProducts(query: String): Flow<List<Product>> = productDao.search(query)

    suspend fun getProduct(id: Long): Product? = productDao.findById(id)

    // Cart
    fun observeCart(userId: Long): Flow<List<CartProduct>> = cartDao.observeCart(userId)
    fun observeCartCount(userId: Long): Flow<Int> = cartDao.observeCartCount(userId)

    suspend fun addToCart(userId: Long, productId: Long, delta: Int = 1) {
        val current = cartDao.getQuantity(userId, productId) ?: 0
        val newQty = current + delta
        if (newQty <= 0) {
            cartDao.remove(userId, productId)
        } else {
            cartDao.upsert(CartItem(userId, productId, newQty))
        }
    }

    suspend fun setCartQuantity(userId: Long, productId: Long, quantity: Int) {
        if (quantity <= 0) {
            cartDao.remove(userId, productId)
        } else {
            cartDao.upsert(CartItem(userId, productId, quantity))
        }
    }

    suspend fun removeFromCart(userId: Long, productId: Long) = cartDao.remove(userId, productId)
    suspend fun clearCart(userId: Long) = cartDao.clear(userId)

    // Favorites
    fun observeFavorites(userId: Long): Flow<List<Product>> = favoriteDao.observeFavorites(userId)

    fun observeIsFavorite(userId: Long, productId: Long): Flow<Boolean> =
        favoriteDao.observeIsFavorite(userId, productId)

    suspend fun addFavorite(userId: Long, productId: Long) =
        favoriteDao.add(FavoriteItem(userId, productId))

    suspend fun removeFavorite(userId: Long, productId: Long) =
        favoriteDao.remove(userId, productId)

    suspend fun toggleFavorite(userId: Long, productId: Long, currentlyFavorite: Boolean) {
        if (currentlyFavorite) removeFavorite(userId, productId) else addFavorite(userId, productId)
    }

    // Orders
    fun observeOrders(userId: Long): Flow<List<OrderWithItems>> = orderDao.observeUserOrders(userId)
    suspend fun getOrder(orderId: Long): OrderWithItems? = orderDao.getOrder(orderId)
    suspend fun updateOrderStatus(orderId: Long, status: String) =
        orderDao.updateStatus(orderId, status)

    /**
     * Создать заказ из текущей корзины пользователя и очистить её.
     * Возвращает id созданного заказа.
     */
    suspend fun createOrderFromCart(
        userId: Long,
        recipientName: String,
        recipientPhone: String,
        deliveryType: String,
        address: String,
        paymentType: String,
        comment: String,
        status: String
    ): Long {
        val cart = cartDao.getCart(userId)
        val total = cart.sumOf { it.lineTotal }
        val orderId = orderDao.insertOrder(
            Order(
                userId = userId,
                createdAt = System.currentTimeMillis(),
                recipientName = recipientName,
                recipientPhone = recipientPhone,
                deliveryType = deliveryType,
                address = address,
                paymentType = paymentType,
                comment = comment,
                total = total,
                status = status
            )
        )
        val items = cart.map {
            OrderItem(
                orderId = orderId,
                productId = it.product.id,
                productName = it.product.name,
                productSku = it.product.sku,
                productImageRes = it.product.imageRes,
                price = it.product.price,
                quantity = it.quantity
            )
        }
        orderDao.insertItems(items)
        cartDao.clear(userId)
        return orderId
    }

    /** Повтор заказа: копирует позиции из прошлого заказа в корзину текущего пользователя. */
    suspend fun repeatOrder(userId: Long, orderId: Long) {
        val order = orderDao.getOrder(orderId) ?: return
        order.items.forEach { item ->
            addToCart(userId, item.productId, item.quantity)
        }
    }
}
