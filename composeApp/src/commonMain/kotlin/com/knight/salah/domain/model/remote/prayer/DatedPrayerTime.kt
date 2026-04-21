package com.knight.salah.domain.model.remote.prayer

import kotlinx.datetime.LocalDate

data class DatedPrayerTime(
    val date: LocalDate,
    val prayer: DailyPrayerTime
)
