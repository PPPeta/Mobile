package ru.stroykrep.app.util

import androidx.appcompat.app.AppCompatDelegate

/**
 * Утилита для применения выбранной темы.
 * mode: 0 = системная, 1 = светлая, 2 = тёмная
 */
object ThemeHelper {

    const val MODE_SYSTEM = 0
    const val MODE_LIGHT = 1
    const val MODE_DARK = 2

    fun applyTheme(mode: Int) {
        val nightMode = when (mode) {
            MODE_LIGHT -> AppCompatDelegate.MODE_NIGHT_NO
            MODE_DARK -> AppCompatDelegate.MODE_NIGHT_YES
            else -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
        }
        AppCompatDelegate.setDefaultNightMode(nightMode)
    }
}
