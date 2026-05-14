package ru.stroykrep.app

import android.app.Application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import android.content.Context
import ru.stroykrep.app.data.AppDatabase
import ru.stroykrep.app.data.DatabaseSeeder
import ru.stroykrep.app.data.repo.AuthRepository
import ru.stroykrep.app.data.repo.ShopRepository
import ru.stroykrep.app.data.session.SessionManager
import ru.stroykrep.app.util.ThemeHelper

class StroyKrepApp : Application() {

    val database: AppDatabase by lazy { AppDatabase.getInstance(this) }
    val sessionManager: SessionManager by lazy { SessionManager(this) }

    val authRepository: AuthRepository by lazy {
        AuthRepository(database.userDao(), sessionManager)
    }

    val shopRepository: ShopRepository by lazy {
        ShopRepository(
            database.categoryDao(),
            database.productDao(),
            database.cartDao(),
            database.favoriteDao(),
            database.orderDao()
        )
    }

    private val appScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onCreate() {
        super.onCreate()
        // Применяем сохранённую тему
        ThemeHelper.applyTheme(sessionManager.getThemeMode())
        appScope.launch {
            DatabaseSeeder.seedIfEmpty(database)
        }
    }
}



/** Удобный доступ к Application из Fragment/Activity. */
val Context.app: StroyKrepApp
    get() = applicationContext as StroyKrepApp
