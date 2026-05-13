package ru.stroykrep.app.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import ru.stroykrep.app.R
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
    }
}
