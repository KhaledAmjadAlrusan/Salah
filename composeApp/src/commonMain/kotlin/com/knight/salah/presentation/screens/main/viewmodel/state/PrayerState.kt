package com.knight.salah.presentation.screens.main.viewmodel.state

import com.knight.salah.domain.model.PrayerTime
import com.knight.salah.domain.model.buildPrayerNotificationsForToday
import com.knight.salah.platform.NotificationManager
import com.knight.salah.util.currentLocalTime
import com.knight.salah.util.toLocalTimeOrNull
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.time.Instant


data class PrayerState(
    val rows: List<PrayerRow> = emptyList(),
    val isLoading: Boolean = false
)

data class PrayerRow(
    val name: String,
    val athan: LocalTime?,   // Adhan
    val iqama: LocalTime?,   // Iqama
    val isNextPrayer: Boolean = false
)


fun PrayerTime.toPrayerRowsWithNext(
    now: LocalTime = currentLocalTime()
): List<PrayerRow> {
    val rows = mutableListOf<PrayerRow>()

    rows += PrayerRow(
        name = "Fajr",
        athan = prayers.fajr.athan.toLocalTimeOrNull(),
        iqama = prayers.fajr.iqama.toLocalTimeOrNull()
    )
    rows += PrayerRow(
        name = "Dhuhr",
        athan = prayers.dhuhr.athan.toLocalTimeOrNull(),
        iqama = prayers.dhuhr.iqama.toLocalTimeOrNull()
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

    // Jumuah
    prayers.jumuahPrayer.forEachIndexed { index, j ->
        val label = if (prayers.jumuahPrayer.size > 1) "Jumu'ah ${index + 1}" else "Jumu'ah"
        rows += PrayerRow(
            name = label,
            athan = j.khutbah.toLocalTimeOrNull(),  // or null if you only care about iqama
            iqama = j.iqama.toLocalTimeOrNull()
        )
    }

    return rows.markNextPrayer(now)
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
        row.copy(isNextPrayer = row.name == nextRow.name && row.iqama == nextRow.iqama && row.athan == nextRow.athan)
    }
}

@OptIn(ExperimentalTime::class)
fun PrayerTime.schedulePrayerNotifications(
    notificationManager: NotificationManager,
    now: Instant = Clock.System.now(),
    zone: TimeZone = TimeZone.currentSystemDefault()
) {
    val notifications = buildPrayerNotificationsForToday(now, zone)

    notifications.forEach { n ->
        notificationManager.scheduleNotification(
            id = n.id,
            triggerAt = n.triggerAt,
            title = n.title,
            description = n.body
        )
    }
}