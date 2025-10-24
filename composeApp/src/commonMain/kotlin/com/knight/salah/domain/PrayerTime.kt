package com.knight.salah.domain

import kotlinx.datetime.LocalTime

data class PrayerTime(
    val name: String,
    val time: LocalTime?,
    val isNextPrayer: Boolean = false,
    val iconRes: Int? = null
)

fun generatePrayerTimes(): List<PrayerTime> {
    return listOf(
        PrayerTime("Fajr", LocalTime(5, 30)),
        PrayerTime("Sunrise", LocalTime(6, 45)),
        PrayerTime("Dhuhr", LocalTime(12, 30), isNextPrayer = true),
        PrayerTime("Asr", LocalTime(15, 45)),
        PrayerTime("Maghrib", LocalTime(18, 20)),
        PrayerTime("Isha", LocalTime(19, 45))
    )
}
