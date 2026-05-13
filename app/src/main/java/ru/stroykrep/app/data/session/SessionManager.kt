package ru.stroykrep.app.data.session

import android.content.Context
import android.content.SharedPreferences

/**
 * Хранит id текущего залогиненного пользователя в SharedPreferences.
 * Используется для фильтрации корзины/избранного/заказов по пользователю.
 */
class SessionManager(context: Context) {

    private val prefs: SharedPreferences =
        context.applicationContext.getSharedPreferences(PREFS, Context.MODE_PRIVATE)

    fun setCurrentUserId(userId: Long) {
        prefs.edit().putLong(KEY_USER_ID, userId).apply()
    }

    fun getCurrentUserId(): Long {
        val id = prefs.getLong(KEY_USER_ID, -1L)
        return id
    }

    fun isLoggedIn(): Boolean = getCurrentUserId() > 0

    fun logout() {
        prefs.edit().remove(KEY_USER_ID).apply()
    }

    companion object {
        private const val PREFS = "stroykrep_prefs"
        private const val KEY_USER_ID = "current_user_id"
    }
}
