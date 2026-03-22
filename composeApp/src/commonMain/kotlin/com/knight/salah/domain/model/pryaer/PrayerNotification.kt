package com.knight.salah.domain.model.pryaer

import com.knight.salah.core.util.toLocalTimeOrNull
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atTime
import kotlinx.datetime.toInstant
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
data class PrayerNotification(
    val id: String,
    val title: String,
    val body: String,
    val triggerAt: Instant
)

@OptIn(ExperimentalTime::class)
fun DailyPrayerTime.buildPrayerNotificationsForDay(
    date: LocalDate,
    zone: TimeZone = TimeZone.currentSystemDefault()
): List<PrayerNotification> {

    fun at(time: LocalTime?): Instant? =
        time?.let { date.atTime(it).toInstant(zone) }

    fun notificationId(
        day: LocalDate,
        name: String,
        kind: String // "athan" or "iqama"
    ): String = "${id}-${day}-$name-$kind"

    val result = mutableListOf<PrayerNotification>()

    fun add(name: String, azan: String?, iqama: String?) {
        val azanInstant = azan?.toLocalTimeOrNull()?.let(::at)
        val iqamaInstant = iqama?.toLocalTimeOrNull()?.let(::at)

        azanInstant?.let { instant ->
            result += PrayerNotification(
                id = notificationId(date, name, "athan"),
                title = "$name – Athan",
                body = "Time for $name",
                triggerAt = instant
            )
        }

        iqamaInstant?.let { instant ->
            result += PrayerNotification(
                id = notificationId(date, name, "iqama"),
                title = "$name – Iqama",
                body = "Iqama for $name",
                triggerAt = instant
            )
        }
    }

    // Always
    add("Fajr", fajrAzan, fajrIqamah)
    add("Asr", asrAzan, asrIqamah)
    add("Maghrib", maghribAzan, maghribIqamah)
    add("Isha", ishaAzan, ishaIqamah)
    add("Jumu'ah", jumahTime1, jumahTime1)

    if (date.dayOfWeek == DayOfWeek.FRIDAY) {
        // Friday: Jumuah instead of Dhuhr
        if (jumahTime2 != null) {
            add("Jumu'ah", jumahTime2, jumahTime2)

        }
        if (jumahTime3 != null) {
            add("Jumu'ah", jumahTime3, jumahTime3)
        }
    } else {
        // Other days: normal Dhuhr, no Jumuah
        add("Dhuhr", dhuhrAzan, dhuhrIqamah)
    }

    return result
}

