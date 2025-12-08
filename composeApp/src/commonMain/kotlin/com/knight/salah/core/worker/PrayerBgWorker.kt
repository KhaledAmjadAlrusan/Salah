package com.knight.salah.core.worker

import com.knight.salah.domain.repoistory.RefreshPrayerUseCase
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

object PrayerBgWorker: KoinComponent {
    val refreshPrayerUseCase: RefreshPrayerUseCase by inject()

    fun handle(daysToSchedule : Int = 1) {
        refreshPrayerUseCase.refreshPrayerTimesAndSchedule(daysToSchedule)
    }

    suspend fun suspendHandle(daysToSchedule : Int = 1){
        refreshPrayerUseCase.suspendedRefreshPrayerTimesAndSchedule(daysToSchedule)
    }
}