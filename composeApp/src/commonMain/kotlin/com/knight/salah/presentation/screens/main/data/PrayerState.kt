package com.knight.salah.presentation.screens.main.data

import com.knight.salah.core.util.toLocalTimeOrNull
import com.knight.salah.domain.model.remote.prayer.DailyPrayerTime
import com.knight.salah.domain.model.remote.prayer.buildPrayerNotificationsForDay
import com.knight.salah.notifications.PrayerNotificationManager
import com.knight.salah.notifications.PrayerNotificationSound
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.datetime.Month
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atTime
import kotlinx.datetime.plus
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

data class PrayerState(
    val rows: List<PrayerRow> = emptyList(),
    val date: String = "",
    val mosqueName: String = "",
    val isLoading: Boolean = false
)

data class PrayerRow(
    val name: String,
    val athan: LocalTime?,
    val iqama: LocalTime?,
    val isNextPrayer: Boolean = false
)

private fun DailyPrayerTime.buildPrayerRows(): List<PrayerRow> {
    val rows = mutableListOf<PrayerRow>()

    rows += PrayerRow(
        name = "Fajr",
        athan = fajrAzan.toLocalTimeOrNull(),
        iqama = fajrIqamah.toLocalTimeOrNull()
    )
    rows += PrayerRow(
        name = "Dhuhr",
        athan = dhuhrAzan.toLocalTimeOrNull(),
        iqama = dhuhrIqamah.toLocalTimeOrNull()
    )
    rows += PrayerRow(
        name = "Asr",
        athan = asrAzan.toLocalTimeOrNull(),
        iqama = asrIqamah.toLocalTimeOrNull()
    )
    rows += PrayerRow(
        name = "Maghrib",
        athan = maghribAzan.toLocalTimeOrNull(),
        iqama = maghribIqamah.toLocalTimeOrNull()
    )
    rows += PrayerRow(
        name = "Isha",
        athan = ishaAzan.toLocalTimeOrNull(),
        iqama = ishaIqamah.toLocalTimeOrNull()
    )

    rows += PrayerRow(
        name = "Jumu'ah",
        athan = jumahTime1.toLocalTimeOrNull(),
        iqama = jumahTime1.toLocalTimeOrNull()
    )

    if (jumahTime2 != null) {
        rows += PrayerRow(
            name = "Jumu'ah",
            athan = jumahTime2.toLocalTimeOrNull(),
            iqama = jumahTime2.toLocalTimeOrNull()
        )
    }
    if (jumahTime3 != null) {
        rows += PrayerRow(
            name = "Jumu'ah",
            athan = jumahTime3.toLocalTimeOrNull(),
            iqama = jumahTime3.toLocalTimeOrNull()
        )
    }

    return rows
}

@OptIn(ExperimentalTime::class)
fun DailyPrayerTime.toPrayerRowsWithNext(
    now: Instant = Clock.System.now(),
    zone: TimeZone = TimeZone.currentSystemDefault()
): List<PrayerRow> {
    val dt = now.toLocalDateTime(zone)
    val date = dt.date
    val time = dt.time

    val rows = buildPrayerRows()
    return rows.markNextPrayer(time, date)
}

fun List<PrayerRow>.markNextPrayer(
    now: LocalTime,
    date: LocalDate
): List<PrayerRow> {
    val isFriday = date.dayOfWeek == DayOfWeek.FRIDAY

    val eligible = filter { row ->
        when {
            isFriday && row.name.startsWith("Dhuhr", ignoreCase = true) -> false
            !isFriday && row.name.startsWith("Jumu'ah", ignoreCase = true) -> false
            else -> true
        }
    }

    val withBaseTime = eligible.mapNotNull { row ->
        val base = row.iqama ?: row.athan
        base?.let { row to it }
    }.sortedBy { it.second }

    if (withBaseTime.isEmpty()) return this

    val (nextRow, _) =
        withBaseTime.firstOrNull { it.second >= now } ?: withBaseTime.first()

    return map { row ->
        row.copy(
            isNextPrayer = row.name == nextRow.name &&
                    row.iqama == nextRow.iqama &&
                    row.athan == nextRow.athan
        )
    }
}

@OptIn(ExperimentalTime::class)
fun DailyPrayerTime.nextSwitchInstant(
    now: Instant,
    zone: TimeZone = TimeZone.currentSystemDefault()
): Instant? {
    val today = now.toLocalDateTime(zone).date

    val points = buildPrayerRows()
        .mapNotNull { row ->
            val base = row.iqama ?: row.athan
            base?.let { t -> today.atTime(t).toInstant(zone) }
        }
        .sorted()

    return points.firstOrNull { it > now }
}

@OptIn(ExperimentalTime::class)
fun DailyPrayerTime.schedulePrayerNotifications(
    notificationManager: PrayerNotificationManager,
    daysToSchedule: Int = 1,
    now: Instant = Clock.System.now(),
    zone: TimeZone = TimeZone.currentSystemDefault(),
    athanSoundEnabled: Boolean,
    iqamaSoundEnabled: Boolean
) {
    require(daysToSchedule >= 1)

    val today = now.toLocalDateTime(zone).date

    for (offset in 0 until daysToSchedule) {
        val date = today.plus(DatePeriod(days = offset))
        val dayNotifications = buildPrayerNotificationsForDay(date, zone)

        dayNotifications.forEach { n ->
            if (date == today && n.triggerAt < now) return@forEach
            val isAthan = n.title.lowercase().endsWith("athan")
            val sound = when {
                isAthan && athanSoundEnabled -> PrayerNotificationSound.ADHAN
                isAthan && !athanSoundEnabled -> PrayerNotificationSound.DEFAULT
                !isAthan && iqamaSoundEnabled -> PrayerNotificationSound.IQAMA
                !isAthan && !iqamaSoundEnabled -> PrayerNotificationSound.DEFAULT
                else -> PrayerNotificationSound.DEFAULT
            }

            notificationManager.scheduleNotification(
                id = n.id,
                triggerAt = n.triggerAt,
                title = n.title,
                description = n.body,
                sound = sound
            )
        }
    }
}

private val monthNames = mapOf(
    Month.JANUARY to "January",
    Month.FEBRUARY to "February",
    Month.MARCH to "March",
    Month.APRIL to "April",
    Month.MAY to "May",
    Month.JUNE to "June",
    Month.JULY to "July",
    Month.AUGUST to "August",
    Month.SEPTEMBER to "September",
    Month.OCTOBER to "October",
    Month.NOVEMBER to "November",
    Month.DECEMBER to "December"
)

@OptIn(ExperimentalTime::class)
fun buildTodayLabel(
    now: Instant = Clock.System.now(),
    zone: TimeZone = TimeZone.currentSystemDefault()
): String {
    val date = now.toLocalDateTime(zone).date
    val month = monthNames[date.month] ?: date.month.name.lowercase()
        .replaceFirstChar { it.titlecase() }

    return "Today, $month ${date.day}, ${date.year}"
}
