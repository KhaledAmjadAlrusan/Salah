package com.knight.salah.domain.model

import com.knight.salah.util.toLocalTimeOrNull
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
data class PrayerAlarm(
    val requestId: String,
    val title: String,
    val body: String,
    val triggerAt: Instant
)


@OptIn(ExperimentalTime::class)
fun PrayerTime.buildAlarmsForToday(
    now: Instant = Clock.System.now(),
    zone: TimeZone = TimeZone.currentSystemDefault()
): List<PrayerAlarm> {
    val today = now.toLocalDateTime(zone).date
    fun at(date: LocalDate, time: LocalTime?) =
        time?.let { date.atTime(it).toInstant(zone) }

    val alarms = mutableListOf<PrayerAlarm>()

    fun add(name: String, azan: String?, iqama: String?) {
        azan?.toLocalTimeOrNull()?.let { t ->
            at(today, t)?.let { instant ->
                alarms += PrayerAlarm(
                    requestId = "$id-$name-athan",
                    title = "$name – Athan",
                    body = "Time for $name",
                    triggerAt = instant
                )
            }
        }
        iqama?.toLocalTimeOrNull()?.let { t ->
            at(today, t)?.let { instant ->
                alarms += PrayerAlarm(
                    requestId = "$id-$name-iqama",
                    title = "$name – Iqama",
                    body = "Iqama for $name",
                    triggerAt = instant
                )
            }
        }
    }

    add("Fajr",    prayers.fajr.athan,    prayers.fajr.iqama)
    add("Dhuhr",   prayers.dhuhr.athan,   prayers.dhuhr.iqama)
    add("Asr",     prayers.asr.athan,     prayers.asr.iqama)
    add("Maghrib", prayers.maghrib.athan, prayers.maghrib.iqama)
    add("Isha",    prayers.isha.athan,    prayers.isha.iqama)

    // Jumuah: use khutbah as “Athan”
    prayers.jumuahPrayer.forEachIndexed { index, j ->
        val name = if (prayers.jumuahPrayer.size > 1) "Jumu'ah ${index + 1}" else "Jumu'ah"
        add(name, j.khutbah, j.iqama)
    }

    return alarms
}
