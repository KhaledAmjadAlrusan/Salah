package com.knight.salah.domain.repository.prayer

import com.knight.salah.domain.model.remote.prayer.DailyPrayerTime
import com.knight.salah.domain.model.remote.prayer.buildPrayerNotificationsForDay
import com.knight.salah.domain.repository.setting.SettingRepository
import com.knight.salah.notifications.PrayerNotificationManager
import com.knight.salah.notifications.PrayerNotificationSound
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
class RefreshPrayerUseCase(
    private val salahRepository: SalahRepository,
    private val settingRepository: SettingRepository,
    private val notificationManager: PrayerNotificationManager
) {
    private val coroutineScope = CoroutineScope(
        SupervisorJob() + Dispatchers.IO
    )

    fun refreshPrayerTimesAndSchedule(
        daysToSchedule: Int = 7,
        forceRefresh: Boolean = false
    ) {
        coroutineScope.launch {
            reschedule(daysToSchedule, forceRefresh)
        }
    }

    suspend fun suspendedRefreshPrayerTimesAndSchedule(
        daysToSchedule: Int = 7,
        forceRefresh: Boolean = false
    ) {
        reschedule(daysToSchedule, forceRefresh)
    }

    private suspend fun reschedule(
        daysToSchedule: Int,
        forceRefresh: Boolean
    ) {
        val datedPrayerTimes = salahRepository.ensurePrayerRangeCached(
            daysCount = daysToSchedule,
            forceRefresh = forceRefresh
        )

        val notificationEnabled = settingRepository.getNotificationEnabled().first()
        val athanEnabled = settingRepository.getAthanSoundEnabled().first()
        val iqamaEnabled = settingRepository.getIqamaSoundEnabled().first()

        notificationManager.cancelAllPrayerNotifications()

        if (!notificationEnabled) return

        datedPrayerTimes.forEach { datedPrayer ->
            datedPrayer.prayer.schedulePrayerNotificationsForDate(
                notificationManager = notificationManager,
                date = datedPrayer.date,
                athanSoundEnabled = athanEnabled,
                iqamaSoundEnabled = iqamaEnabled
            )
        }
    }
}

@OptIn(ExperimentalTime::class)
fun DailyPrayerTime.schedulePrayerNotificationsForDate(
    notificationManager: PrayerNotificationManager,
    date: LocalDate,
    now: Instant = Clock.System.now(),
    zone: TimeZone = TimeZone.currentSystemDefault(),
    athanSoundEnabled: Boolean,
    iqamaSoundEnabled: Boolean
) {
    val today = now.toLocalDateTime(zone).date
    val dayNotifications = buildPrayerNotificationsForDay(date, zone)

    dayNotifications.forEach { notification ->
        if (date == today && notification.triggerAt < now) return@forEach

        val isAthan = notification.title.lowercase().endsWith("athan")
        val sound = when {
            isAthan && athanSoundEnabled -> PrayerNotificationSound.ADHAN
            !isAthan && iqamaSoundEnabled -> PrayerNotificationSound.IQAMA
            else -> PrayerNotificationSound.DEFAULT
        }

        notificationManager.scheduleNotification(
            id = notification.id,
            triggerAt = notification.triggerAt,
            title = notification.title,
            description = notification.body,
            sound = sound
        )
    }
}
