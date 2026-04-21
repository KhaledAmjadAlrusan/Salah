package com.knight.salah.notifications

import kotlin.time.ExperimentalTime
import kotlin.time.Instant

expect class PrayerNotificationManager {
    fun showNotification(
        title: String,
        description: String,
        sound: PrayerNotificationSound = PrayerNotificationSound.DEFAULT
    )

    @OptIn(ExperimentalTime::class)
    fun scheduleNotification(
        id: String,
        triggerAt: Instant,
        title: String,
        description: String,
        sound: PrayerNotificationSound
    )

    fun cancelScheduledNotification(id: String)
    fun cancelAllPrayerNotifications()
}

enum class PrayerNotificationSound {
    DEFAULT,
    ADHAN,
    IQAMA
}
