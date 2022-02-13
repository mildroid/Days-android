package com.mildroid.days.utils

import kotlinx.datetime.*
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month
import java.text.SimpleDateFormat
import java.time.*
import java.time.DayOfWeek
import java.time.chrono.ChronoLocalDate
import java.time.temporal.TemporalAccessor
import java.time.temporal.TemporalAdjusters
import java.util.*
import kotlin.math.abs
import kotlin.time.Duration.Companion.days

fun safeLocalDate(day: Int, month: Int): LocalDate {
    require(day in 1..31)
    require(month in 1..12)

    val desire = MonthDay.of(month, day)
    var currentYear = Year.now().value

    if (MonthDay.now().isAfter(desire))
        currentYear += 1

    return LocalDate(currentYear, month, day)
}

fun safeLocalDate(day: DayOfWeek, place: Int, month: Month): LocalDate {
    val desireDay = java.time.LocalDate.of(Year.now().value, month, day.value)
        .with(TemporalAdjusters.firstInMonth(day))
        .dayOfMonth + ((place - 1) * 7)

    return safeLocalDate(desireDay, month)
}

fun safeLocalDate(day: Int, month: Month) = safeLocalDate(day, month.value)

fun LocalDate.daysUntilNow() =
    abs(
        this.daysUntil(
            java.time.LocalDate.now().toKotlinLocalDate()
        )
    ).days

fun LocalDate.inDaysRemaining(): String {
    return "in ${daysUntilNow().inWholeDays} Days"
}