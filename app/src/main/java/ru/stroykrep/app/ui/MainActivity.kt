package ru.stroykrep.app.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import kotlinx.coroutines.launch
import ru.stroykrep.app.R
import ru.stroykrep.app.app
import ru.stroykrep.app.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHost = supportFragmentManager
            .findFragmentById(R.id.navHostFragment) as NavHostFragment
        val navController = navHost.navController

        binding.bottomNavigation.setupWithNavController(navController)

        // Показываем bottom nav только на табах первого уровня
        val topLevelDestinations = setOf(
            R.id.homeFragment,
            R.id.categoriesFragment,
            R.id.cartFragment,
            R.id.profileFragment
        )
        navController.addOnDestinationChangedListener { _, destination, _ ->
            binding.bottomNavigation.visibility =
                if (destination.id in topLevelDestinations)
                    android.view.View.VISIBLE
                else
                    android.view.View.GONE
        }

        // Badge на корзине — показывает количество товаров
        observeCartBadge()
    }

    private fun observeCartBadge() {
        val userId = app.sessionManager.getCurrentUserId()
        if (userId <= 0) return

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
                }
            }
        }
    }
}
