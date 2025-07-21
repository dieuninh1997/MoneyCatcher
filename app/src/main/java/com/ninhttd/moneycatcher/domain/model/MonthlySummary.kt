package com.ninhttd.moneycatcher.domain.model

import java.time.YearMonth

data class MonthlySummary(
    val month: YearMonth,
    val income: Long,
    val expense: Long
)