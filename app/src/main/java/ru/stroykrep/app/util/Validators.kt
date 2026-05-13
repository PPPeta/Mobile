package ru.stroykrep.app.util

object Validators {

    private val EMAIL_REGEX = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")

    fun isValidEmail(value: String): Boolean {
        return value.isNotBlank() && EMAIL_REGEX.matches(value.trim())
    }

    fun isValidPhone(value: String): Boolean {
        val digits = value.filter { it.isDigit() }
        return digits.length in 10..12
    }

    fun isValidPassword(value: String): Boolean {
        return value.length >= 6
    }

    /** Проверка номера банковской карты по алгоритму Луна. */
    fun isValidCardNumber(value: String): Boolean {
        val digits = value.filter { it.isDigit() }
        if (digits.length !in 13..19) return false
        var sum = 0
        var alt = false
        for (i in digits.length - 1 downTo 0) {
            var n = digits[i].digitToInt()
            if (alt) {
                n *= 2
                if (n > 9) n -= 9
            }
            sum += n
            alt = !alt
        }
        return sum % 10 == 0
    }

    /** Проверка срока действия карты в формате MM/YY. */
    fun isValidExpiry(value: String): Boolean {
        val parts = value.split("/")
        if (parts.size != 2) return false
        val month = parts[0].toIntOrNull() ?: return false
        val year = parts[1].toIntOrNull() ?: return false
        if (month !in 1..12) return false
        if (year !in 0..99) return false
        return true
    }

    fun isValidCvc(value: String): Boolean {
        return value.length in 3..4 && value.all { it.isDigit() }
    }
}
