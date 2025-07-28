package com.ninhttd.moneycatcher.ui.screen.report.components

import com.ninhttd.moneycatcher.common.PeriodType
import java.time.LocalDate
import java.time.YearMonth
import java.time.temporal.WeekFields
import java.util.Locale

data class PeriodFilterState(
    val type: PeriodType,
    val value: String,
    val startDate: LocalDate,
    val endDate: LocalDate
) {
    companion object {
        fun default(): PeriodFilterState {
            return forType(PeriodType.MONTH)
        }

        fun forType(type: PeriodType): PeriodFilterState {
            val today = LocalDate.now()
            return when (type) {
                PeriodType.MONTH -> {
                    val month = today.monthValue
                    val year = today.year
                    val ym = YearMonth.of(year, month)
                    PeriodFilterState(
                        type = type,
                        value = "Tháng $month",
                        startDate = ym.atDay(1),
                        endDate = ym.atEndOfMonth()
                    )
                }

                PeriodType.YEAR -> {
                    val year = today.year
                    PeriodFilterState(
                        type = type,
                        value = "$year",
                        startDate = LocalDate.of(year, 1, 1),
                        endDate = LocalDate.of(year, 12, 31)
                    )
                }

                PeriodType.WEEK -> {
                    val weekFields = WeekFields.of(Locale.getDefault())
                    val weekOfYear = today.get(weekFields.weekOfWeekBasedYear())
                    val firstDayOfWeek = today.with(weekFields.weekOfWeekBasedYear(),
                        weekOfYear.toLong()
                    )
                        .with(weekFields.dayOfWeek(), 1)
                    val lastDayOfWeek = firstDayOfWeek.plusDays(6)

                    PeriodFilterState(
                        type = type,
                        value = "Tuần $weekOfYear",
                        startDate = firstDayOfWeek,
                        endDate = lastDayOfWeek
                    )
                }
            }
        }

        fun forSelection(type: PeriodType, value: String): PeriodFilterState {
            val now = LocalDate.now()
            val year = now.year
            return when (type) {
                PeriodType.MONTH -> {
                    val month = value.filter { it.isDigit() }.toInt()
                    val ym = YearMonth.of(year, month)
                    PeriodFilterState(
                        type = type,
                        value = value,
                        startDate = ym.atDay(1),
                        endDate = ym.atEndOfMonth()
                    )
                }

                PeriodType.YEAR -> {
                    val selectedYear = value.toInt()
                    PeriodFilterState(
                        type = type,
                        value = value,
                        startDate = LocalDate.of(selectedYear, 1, 1),
                        endDate = LocalDate.of(selectedYear, 12, 31)
                    )
                }

                PeriodType.WEEK -> {
                    val week = value.filter { it.isDigit() }.toInt()
                    val weekFields = WeekFields.of(Locale.getDefault())
                    val firstDayOfYear = LocalDate.of(year, 1, 4) // guaranteed to be in week 1
                    val firstDayOfWeek = firstDayOfYear
                        .with(weekFields.weekOfWeekBasedYear(), week.toLong())
                        .with(weekFields.dayOfWeek(), 1)
                    val lastDayOfWeek = firstDayOfWeek.plusDays(6)

                    PeriodFilterState(
                        type = type,
                        value = value,
                        startDate = firstDayOfWeek,
                        endDate = lastDayOfWeek
                    )
                }
            }
        }
    }
}
