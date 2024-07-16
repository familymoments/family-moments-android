package io.familymoments.app.feature.calendar.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.familymoments.app.R
import io.familymoments.app.core.theme.AppColors
import io.familymoments.app.core.theme.AppTypography
import io.familymoments.app.feature.calendar.viewmodel.CalendarViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun CalendarScreen(
    modifier: Modifier,
    viewModel: CalendarViewModel,
    navigateToCalendarDay: (String) -> Unit
) {
    val calendarUiState = viewModel.calendarUiState.collectAsStateWithLifecycle()
    val daysOfweek = listOf(SUNDAY, MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY)
    val isLoading = calendarUiState.value.isLoading
    val postResult = calendarUiState.value.postResult

    if (isLoading != false) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(64.dp),
                color = MaterialTheme.colorScheme.secondary,
                trackColor = MaterialTheme.colorScheme.surfaceVariant,
            )
        }
    } else {
        Column(
            modifier = modifier
                .padding(horizontal = 16.dp)
                .fillMaxSize(),
        ) {
            Text(
                modifier = Modifier
                    .padding(vertical = 18.dp)
                    .align(Alignment.CenterHorizontally),
                text = stringResource(id = R.string.calendar_title),
                style = AppTypography.B1_16,
                color = AppColors.black1
            )
            CalendarHeader(
                formattedYearMonth = calendarUiState.value.yearMonth.format(DateTimeFormatter.ofPattern("yyyy.MM")),
                onClickPrevMonth = viewModel::getPreviousMonth,
                onClickNextMonth = viewModel::getNextMonth
            )
            CalendarContent(
                daysOfweek = daysOfweek,
                dates = calendarUiState.value.dates,
                today = calendarUiState.value.today,
                isTodayInMonth = calendarUiState.value.isTodayInMonth,
                postResult = postResult,
                navigateToCalendarDay = navigateToCalendarDay
            )
        }
    }
}

@Composable
private fun CalendarContent(
    daysOfweek: List<String>,
    dates: List<LocalDate>,
    today: LocalDate,
    isTodayInMonth: Boolean,
    postResult: List<String>,
    navigateToCalendarDay: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(16.5.dp))
            .background(Color.Transparent)
    ) {
        Image(
            painter = painterResource(id = R.drawable.img_calendar_background),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.matchParentSize()
        )
        Column(
            modifier = Modifier
                .padding(horizontal = 14.dp, vertical = 8.dp)
        ) {
            Row(
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .heightIn(min = (32.8).dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                daysOfweek.forEach { day ->
                    Text(
                        modifier = Modifier.weight(1f),
                        text = day,
                        style = AppTypography.SH3_16,
                        color = AppColors.black1,
                        textAlign = TextAlign.Center
                    )
                }
            }

            LazyVerticalGrid(
                columns = GridCells.Fixed(7),
            ) {
                items(dates.size) { index ->
                    if (dates[index] == LocalDate.MIN) return@items

                    val text = transformDate(dates.map { it.dayOfMonth.toString() }[index])
                    val isToday = dates[index] == today && isTodayInMonth
                    Column(
                        modifier = Modifier
                            .padding(all = (3.2).dp)
                            .heightIn(min = 43.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(
                                if (isToday) AppColors.purple2.copy(alpha = 0.7f)
                                else Color.Unspecified
                            )
                            .clickable { navigateToCalendarDay(dates[index].toString()) },
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text(
                            text = text,
                            style = AppTypography.BTN5_16,
                            color = if (isToday) AppColors.grey6 else AppColors.black1,
                        )
                        if (postResult.contains(dates[index].toString())) {
                            Icon(
                                modifier = Modifier.padding(top = 6.dp),
                                imageVector = ImageVector.vectorResource(R.drawable.ic_calendar_dot),
                                contentDescription = null,
                                tint = Color.Unspecified
                            )
                        }
                    }
                }
            }
        }
    }
}

private fun transformDate(day: String): String {
    return if (day.isEmpty()) {
        ""
    } else if (day.toInt() in 1..9) {
        "0$day"
    } else {
        day
    }
}

@Composable
private fun CalendarHeader(
    formattedYearMonth: String,
    onClickPrevMonth: () -> Unit,
    onClickNextMonth: () -> Unit,
) {
    Row(
        modifier = Modifier
            .padding(vertical = 24.dp)
            .height(64.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(color = AppColors.grey9),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconButton(modifier = Modifier.size(32.dp), onClick = onClickPrevMonth) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_calendar_previous_arrow),
                contentDescription = null,
                tint = Color.Unspecified
            )
        }
        Text(
            modifier = Modifier.weight(1f),
            text = formattedYearMonth,
            style = AppTypography.H2_24,
            color = AppColors.grey6,
            textAlign = TextAlign.Center
        )
        IconButton(modifier = Modifier.size(32.dp), onClick = onClickNextMonth) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_calendar_next_arrow),
                contentDescription = null,
                tint = Color.Unspecified
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CalendarScreenPreview() {
    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxSize(),
    ) {
        Text(
            modifier = Modifier
                .padding(vertical = 18.dp)
                .align(Alignment.CenterHorizontally),
            text = "캘린더",
            style = AppTypography.B1_16,
            color = AppColors.black1
        )
        CalendarHeader(
            formattedYearMonth = "2024.03",
            onClickPrevMonth = {},
            onClickNextMonth = {}
        )
        CalendarContent(
            daysOfweek = listOf("Su", "Mo", "Tu", "We", "Th", "Fr", "Sa"),
            dates = (1..31).map { LocalDate.of(2024, 3, it) },
            today = LocalDate.now(),
            isTodayInMonth = true,
            postResult = listOf("2024-03-01", "2024-03-02", "2024-03-03"),
            navigateToCalendarDay = {}
        )
    }
}

private const val SUNDAY = "Su"
private const val MONDAY = "Mo"
private const val TUESDAY = "Tu"
private const val WEDNESDAY = "We"
private const val THURSDAY = "Th"
private const val FRIDAY = "Fr"
private const val SATURDAY = "Sa"
