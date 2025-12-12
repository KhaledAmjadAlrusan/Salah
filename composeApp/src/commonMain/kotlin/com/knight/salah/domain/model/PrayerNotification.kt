package com.knight.salah.domain.model

import com.knight.salah.core.util.toLocalTimeOrNull
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atTime
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
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
fun PrayerTime.buildPrayerNotificationsForToday(
    now: Instant = Clock.System.now(),
    zone: TimeZone = TimeZone.currentSystemDefault()
): List<PrayerNotification> {
    val today = now.toLocalDateTime(zone).date

    fun at(time: LocalTime?): Instant? =
        time?.let { today.atTime(it).toInstant(zone) }

    val result = mutableListOf<PrayerNotification>()

    fun add(name: String, azan: String?, iqama: String?) {
        val azanTime = azan?.toLocalTimeOrNull()?.let(::at)
        val iqamaTime = iqama?.toLocalTimeOrNull()?.let(::at)

        // Only schedule in the future (or now)
        azanTime?.takeIf { it >= now }?.let { instant ->
            result += PrayerNotification(
                id = "$id-$name-athan",
                title = "$name – Athan",
                body = "Time for $name",
                triggerAt = instant
            )
        }

        iqamaTime?.takeIf { it >= now }?.let { instant ->
            result += PrayerNotification(
                id = "$id-$name-iqama",
                title = "$name – Iqama",
                body = "Iqama for $name",
                triggerAt = instant
            )
        }
    }

    add("Fajr",    prayers.fajr.athan,    prayers.fajr.iqama)
    add("Dhuhr",   prayers.dhuhr.athan,   prayers.dhuhr.iqama)
    add("Asr",     prayers.asr.athan,     prayers.asr.iqama)
    add("Maghrib", prayers.maghrib.athan, prayers.maghrib.iqama)
    add("Isha",    prayers.isha.athan,    prayers.isha.iqama)

    // Jumuah: khutbah as “Athan”
    prayers.jumuahPrayer.forEachIndexed { index, j ->
        val name = if (prayers.jumuahPrayer.size > 1) "Jumu'ah ${index + 1}" else "Jumu'ah"
        add(name, j.khutbah, j.iqama)
    }

    return result
}

@OptIn(ExperimentalTime::class)
fun PrayerTime.buildPrayerNotificationsForDay(
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
    add("Fajr",    prayers.fajr.athan,    prayers.fajr.iqama)
    add("Asr",     prayers.asr.athan,     prayers.asr.iqama)
    add("Maghrib", prayers.maghrib.athan, prayers.maghrib.iqama)
    add("Isha",    prayers.isha.athan,    prayers.isha.iqama)

    if (date.dayOfWeek == DayOfWeek.FRIDAY) {
        // Friday: Jumuah instead of Dhuhr
        prayers.jumuahPrayer.forEachIndexed { index, j ->
            val name =
                if (prayers.jumuahPrayer.size > 1) "Jumu'ah ${index + 1}" else "Jumu'ah"
            add(name, j.khutbah, j.iqama)
        }
    } else {
        // Other days: normal Dhuhr, no Jumuah
        add("Dhuhr", prayers.dhuhr.athan, prayers.dhuhr.iqama)
    }

    return result
}

