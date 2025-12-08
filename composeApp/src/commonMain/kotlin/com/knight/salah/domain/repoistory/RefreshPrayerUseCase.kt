package com.knight.salah.domain.repoistory

import com.knight.salah.platform.NotificationManager
import com.knight.salah.presentation.screens.main.viewmodel.state.schedulePrayerNotifications
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
class RefreshPrayerUseCase(
    private val repository: SalahRepository,
    private val notificationManager: NotificationManager
) {
    val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    fun refreshPrayerTimesAndSchedule(daysToSchedule: Int = 1) {
        coroutineScope.launch {
            val prayerTime = repository.getPrayers() ?: return@launch

            // update notifications for today
            prayerTime.schedulePrayerNotifications(notificationManager,daysToSchedule)
        }
    }

    suspend fun suspendedRefreshPrayerTimesAndSchedule(daysToSchedule: Int = 1) {
        val prayerTime = repository.getPrayers() ?: return
        // update notifications for today
        prayerTime.schedulePrayerNotifications(notificationManager,daysToSchedule)
    }
}