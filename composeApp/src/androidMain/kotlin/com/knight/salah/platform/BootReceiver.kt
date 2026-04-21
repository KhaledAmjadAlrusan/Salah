package com.knight.salah.platform

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.knight.salah.domain.repoistory.prayer.RefreshPrayerUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject


//BootReceiver handles device reboot (reschedules all alarms)
class BootReceiver : BroadcastReceiver(), KoinComponent {
    private val refreshUseCase: RefreshPrayerUseCase by inject()
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            android.util.Log.d("BootReceiver", "Device booted, rescheduling prayers")

            scope.launch {
                refreshUseCase.suspendedRefreshPrayerTimesAndSchedule(daysToSchedule = 7)
            }
        }
    }
}