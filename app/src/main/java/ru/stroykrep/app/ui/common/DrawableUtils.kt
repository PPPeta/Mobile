package ru.stroykrep.app.ui.common

import android.content.Context
import ru.stroykrep.app.R

/**
 * Резолвит drawable по имени (строка из БД, например "ic_bolt").
 * Если не найден — возвращает плейсхолдер.
 */
fun Context.drawableIdByName(name: String, fallback: Int = R.drawable.ic_placeholder_product): Int {
    val id = resources.getIdentifier(name, "drawable", packageName)
    return if (id != 0) id else fallback
}
