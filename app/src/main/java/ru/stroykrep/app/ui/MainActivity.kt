package ru.stroykrep.app.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import kotlinx.coroutines.launch
import ru.stroykrep.app.R
import ru.stroykrep.app.app
import ru.stroykrep.app.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val topLevelDestinations = setOf(
        R.id.homeFragment,
        R.id.categoriesFragment,
        R.id.cartFragment,
        R.id.profileFragment
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHost = supportFragmentManager
            .findFragmentById(R.id.navHostFragment) as NavHostFragment
        val navController = navHost.navController

        // Кастомная обработка кликов BottomNav — стабильнее, чем setupWithNavController.
        // Каждый таб переключается с popUpTo(home) — стек всегда чистый.
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            if (item.itemId == navController.currentDestination?.id) return@setOnItemSelectedListener true

            val options = NavOptions.Builder()
                .setLaunchSingleTop(true)
                .setRestoreState(false)
                .setPopUpTo(R.id.nav_graph, inclusive = false, saveState = false)
                .setEnterAnim(R.anim.fade_in)
                .setExitAnim(R.anim.fade_out)
                .build()
            try {
                navController.navigate(item.itemId, null, options)
                true
            } catch (e: Exception) {
                false
            }
        }

        // Подсветка активного таба + видимость bottom nav
        navController.addOnDestinationChangedListener { _, destination, _ ->
            val isTopLevel = destination.id in topLevelDestinations
            binding.bottomNavigation.visibility = if (isTopLevel) View.VISIBLE else View.GONE
            if (isTopLevel) {
                // setCheckedItem вызывает listener; чтобы не зациклиться — делаем тихо
                val menu = binding.bottomNavigation.menu
                for (i in 0 until menu.size()) {
                    val item = menu.getItem(i)
                    if (item.itemId == destination.id) {
                        if (!item.isChecked) item.isChecked = true
                        break
                    }
                }
            }
        }

        // Бейдж корзины
        observeCartBadge()
    }

    private fun observeCartBadge() {
        val userId = app.sessionManager.getCurrentUserId()
        if (userId <= 0) return

        var prevCount = -1
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                app.shopRepository.observeCartCount(userId).collect { count ->
                    val badge = binding.bottomNavigation.getOrCreateBadge(R.id.cartFragment)
                    if (count > 0) {
                        badge.isVisible = true
                        badge.number = count
                        badge.backgroundColor = getColor(R.color.brand_accent)
                        badge.badgeTextColor = getColor(R.color.white)
                    } else {
                        badge.isVisible = false
                        badge.clearNumber()
                    }
                    // При увеличении кол-ва товаров — pulse-анимация иконки корзины
                    if (prevCount in 0 until count) {
                        pulseCartIcon()
                    }
                    prevCount = count
                }
            }
        }
    }

    private fun pulseCartIcon() {
        val cartView = binding.bottomNavigation.findViewById<View>(R.id.cartFragment) ?: return
        cartView.animate()
            .scaleX(1.3f).scaleY(1.3f)
            .setDuration(150)
            .withEndAction {
                cartView.animate()
                    .scaleX(1f).scaleY(1f)
                    .setDuration(200)
                    .start()
            }
            .start()
    }
}
