package com.knight.salah.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.knight.salah.core.worker.PrayerBgWorker
import java.util.concurrent.TimeUnit

class PrayerRefreshWorker(
    appContext: Context,
    params: WorkerParameters
) : CoroutineWorker(appContext, params) {

    companion object{
        fun scheduleDailyPrayerRefresh(context: Context) {
            val request = PeriodicWorkRequestBuilder<PrayerRefreshWorker>(
                1, TimeUnit.DAYS
            )
                .setInitialDelay(1, TimeUnit.HOURS) // or compute till next midnight
                .build()

            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                "daily_prayer_refresh",
                ExistingPeriodicWorkPolicy.KEEP,
                request
            )
        }
    }

    override suspend fun doWork(): Result {
        return try {
            PrayerBgWorker.suspendHandle()
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }
}
