package com.knight.salah.presentation.screens.main.viewmodel.state

import com.knight.salah.domain.model.PrayerTime
import com.knight.salah.platform.NotificationManager
import com.knight.salah.core.util.currentLocalTime
import com.knight.salah.core.util.toLocalTimeOrNull
import com.knight.salah.domain.model.buildPrayerNotificationsForDay
import com.knight.salah.platform.NotificationSoundType
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
    val date:String = "",
    val isLoading: Boolean = false
)

data class PrayerRow(
    val name: String,
    val athan: LocalTime?,   // Adhan
    val iqama: LocalTime?,   // Iqama
    val isNextPrayer: Boolean = false
)


private fun PrayerTime.buildPrayerRows(
    date: LocalDate
): List<PrayerRow> {
    val rows = mutableListOf<PrayerRow>()

    // Always
    rows += PrayerRow(
        name = "Fajr",
        athan = prayers.fajr.athan.toLocalTimeOrNull(),
        iqama = prayers.fajr.iqama.toLocalTimeOrNull()
    )
    rows += PrayerRow(
        name = "Asr",
        athan = prayers.asr.athan.toLocalTimeOrNull(),
        iqama = prayers.asr.iqama.toLocalTimeOrNull()
    )
    rows += PrayerRow(
        name = "Maghrib",
        athan = prayers.maghrib.athan.toLocalTimeOrNull(),
        iqama = prayers.maghrib.iqama.toLocalTimeOrNull()
    )
    rows += PrayerRow(
        name = "Isha",
        athan = prayers.isha.athan.toLocalTimeOrNull(),
        iqama = prayers.isha.iqama.toLocalTimeOrNull()
    )

    if (date.dayOfWeek == DayOfWeek.FRIDAY) {
        // Friday → Jumuah instead of Dhuhr
        prayers.jumuahPrayer.forEachIndexed { index, j ->
            val label =
                if (prayers.jumuahPrayer.size > 1) "Jumu'ah ${index + 1}" else "Jumu'ah"
            rows += PrayerRow(
                name = label,
                athan = j.khutbah.toLocalTimeOrNull(),
                iqama = j.iqama.toLocalTimeOrNull()
            )
        }
    } else {
        // Other days → normal Dhuhr, no Jumuah
        rows += PrayerRow(
            name = "Dhuhr",
            athan = prayers.dhuhr.athan.toLocalTimeOrNull(),
            iqama = prayers.dhuhr.iqama.toLocalTimeOrNull()
        )
    }

    return rows
}

// overload using Instant, for the watcher
@OptIn(ExperimentalTime::class)
fun PrayerTime.toPrayerRowsWithNext(
    now: Instant = Clock.System.now(),
    zone: TimeZone = TimeZone.currentSystemDefault()
): List<PrayerRow> {
    val dt = now.toLocalDateTime(zone)
    val date = dt.date
    val time = dt.time

    val rows = buildPrayerRows(date)
    return rows.markNextPrayer(time)
}


// Extension on List<PrayerRow> to mark which one is next
fun List<PrayerRow>.markNextPrayer(
    now: LocalTime = currentLocalTime()
): List<PrayerRow> {
    val withBaseTime = mapNotNull { row ->
        val base = row.iqama ?: row.athan
        base?.let { row to it }
    }.sortedBy { it.second }

    if (withBaseTime.isEmpty()) return this

    val nextPair = withBaseTime.firstOrNull { it.second >= now } ?: withBaseTime.first()
    val (nextRow, _) = nextPair

    return map { row ->
        row.copy(
            isNextPrayer = row.name == nextRow.name &&
                    row.iqama == nextRow.iqama &&
                    row.athan == nextRow.athan
        )
    }
}

@OptIn(ExperimentalTime::class)
fun PrayerTime.nextSwitchInstant(
    now: Instant,
    zone: TimeZone = TimeZone.currentSystemDefault()
): Instant? {
    val today = now.toLocalDateTime(zone).date

    val points = buildPrayerRows(today)
        .mapNotNull { row ->
            val base = row.iqama ?: row.athan
            base?.let { t -> today.atTime(t).toInstant(zone) }
        }
        .sorted()

    return points.firstOrNull { it > now }
}

@OptIn(ExperimentalTime::class)
fun PrayerTime.schedulePrayerNotifications(
    notificationManager: NotificationManager,
    daysToSchedule: Int = 1,
    now: Instant = Clock.System.now(),
    zone: TimeZone = TimeZone.currentSystemDefault()
) {
    require(daysToSchedule >= 1)

    val today = now.toLocalDateTime(zone).date

    for (offset in 0 until daysToSchedule) {
        val date = today.plus(DatePeriod(days = offset))
        val dayNotifications = buildPrayerNotificationsForDay(date, zone)

        dayNotifications.forEach { n ->
            // For *today*, skip times already in the past
            if (date == today && n.triggerAt < now) return@forEach
            val isAthan = n.title.lowercase().endsWith("athan")
            val sound = if (isAthan) NotificationSoundType.ADHAN else NotificationSoundType.IQAMA

            notificationManager.scheduleNotification(
                id = n.id,
                triggerAt = n.triggerAt,
                title = n.title,
                description = n.body,
                soundType = sound
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
