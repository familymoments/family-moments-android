package io.familymoments.app.feature.calendar.model

import java.time.LocalDate
import java.time.YearMonth

data class CalendarUiState(
    val isSuccess: Boolean? = null,
    val isLoading: Boolean? = null,
    val errorMessage: String? = null,
    val today: String = LocalDate.now().dayOfMonth.toString(),
    val yearMonth: YearMonth = YearMonth.now(),
    val dates: List<LocalDate> = emptyList(),
    val postResult: List<String> = emptyList()
) {
    val startDate: LocalDate = if (dates.isNotEmpty()) dates.first() else LocalDate.now()
    val endDate: LocalDate = if (dates.isNotEmpty()) dates.last() else LocalDate.now()

    val isTodayInMonth: Boolean = yearMonth.year == LocalDate.now().year && yearMonth.month == LocalDate.now().month
}
