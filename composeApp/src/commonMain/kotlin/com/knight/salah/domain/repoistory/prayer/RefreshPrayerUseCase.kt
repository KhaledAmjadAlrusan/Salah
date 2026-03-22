package com.knight.salah.domain.repoistory.prayer

import com.knight.salah.domain.repoistory.setting.SettingRepository
import com.knight.salah.platform.NotificationManager
import com.knight.salah.presentation.screens.main.data.schedulePrayerNotifications
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlin.time.ExperimentalTime

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
        val prayerTime = salahRepository.getPrayers() ?: return

        val notificationEnabled = settingRepository.getNotificationEnabled().first()
        val athanEnabled = settingRepository.getAthanSoundEnabled().first()
        val iqamaEnabled = settingRepository.getIqamaSoundEnabled().first()

        notificationManager.cancelAllPrayerNotifications()

        if (!notificationEnabled) {
            return
        }

        prayerTime.schedulePrayerNotifications(
            notificationManager = notificationManager,
            daysToSchedule = daysToSchedule,
            athanSoundEnabled = athanEnabled,
            iqamaSoundEnabled = iqamaEnabled
        )
    }
}