package ru.stroykrep.app.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object Formatters {

    private val priceFormat = java.text.DecimalFormat("#,##0.##").apply {
        val symbols = decimalFormatSymbols
        symbols.groupingSeparator = ' '
        symbols.decimalSeparator = ','
        decimalFormatSymbols = symbols
    }

    fun formatPrice(value: Double): String = priceFormat.format(value)

    private val dateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale("ru"))

    fun formatDate(epochMillis: Long): String = dateFormat.format(Date(epochMillis))
}
