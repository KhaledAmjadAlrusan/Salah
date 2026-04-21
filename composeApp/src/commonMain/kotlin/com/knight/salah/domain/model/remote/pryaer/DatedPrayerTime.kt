package com.knight.salah.domain.model.remote.pryaer

import kotlinx.datetime.LocalDate

data class DatedPrayerTime(
    val date: LocalDate,
    val prayer: DailyPrayerTime
)