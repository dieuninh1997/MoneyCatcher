package com.ninhttd.moneycatcher.common

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

object Constants {
    const val BASE_URL = "https://api.coinranking.com/v2/"
    const val PARAM_COIN_ID = "coinId"
    const val COIN_DATABASE_NAME = "Coin.db"
    const val EXPENSE_TAB = 0
    const val INCOME_TAB = 1
}


enum class TransactionType(val id: Int, val txName: String, val displayName: String) {
    INCOME(1, "income", "Thu nhập"),
    EXPENSE(2, "expense", "Chi tiêu");

    companion object {
        fun fromId(id: Int): TransactionType? =
            TransactionType.entries.find { it.id == id }
    }
}

enum class TimeFilter {
    WEEKLY, MONTHLY, YEARLY
}


fun formatDate(input: String): String {
    return try {
        val parsed = LocalDate.parse(input)
        val formatter = DateTimeFormatter.ofPattern("EEEE, dd 'tháng' MM yyyy", Locale("vi"))
        parsed.format(formatter)
    } catch (e: Exception) {
        input // fallback
    }
}

enum class PeriodType(val displayName: String) {
    WEEK("Hàng tuần"),
    MONTH("Hàng tháng"),
    YEAR("Hàng năm")
}