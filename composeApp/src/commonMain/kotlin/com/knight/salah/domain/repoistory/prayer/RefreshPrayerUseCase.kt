package com.knight.salah.domain.repoistory.prayer

import com.knight.salah.domain.model.pryaer.DailyPrayerTime
import com.knight.salah.domain.model.pryaer.buildPrayerNotificationsForDay
import com.knight.salah.domain.repoistory.setting.SettingRepository
import com.knight.salah.platform.NotificationManager
import com.knight.salah.platform.NotificationSoundType
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
    private val notificationManager: NotificationManager
) {

    private val coroutineScope = CoroutineScope(
        SupervisorJob() + Dispatchers.IO
    )

    fun refreshPrayerTimesAndSchedule(daysToSchedule: Int = 1) {
        coroutineScope.launch {
            reschedule(daysToSchedule)
        }
    }

    suspend fun suspendedRefreshPrayerTimesAndSchedule(
        daysToSchedule: Int = 1
    ) {
        reschedule(daysToSchedule)
    }

    private suspend fun reschedule(daysToSchedule: Int) {
        val datedPrayerTimes = salahRepository.getPrayers(daysToSchedule)

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
    notificationManager: NotificationManager,
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
            isAthan && athanSoundEnabled -> NotificationSoundType.ADHAN
            !isAthan && iqamaSoundEnabled -> NotificationSoundType.IQAMA
            else -> NotificationSoundType.DEFAULT
        }

        notificationManager.scheduleNotification(
            id = notification.id,
            triggerAt = notification.triggerAt,
            title = notification.title,
            description = notification.body,
            soundType = sound
        )
    }
}