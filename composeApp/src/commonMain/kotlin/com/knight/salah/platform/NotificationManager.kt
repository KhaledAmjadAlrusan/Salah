package com.knight.salah.platform

import kotlin.time.ExperimentalTime
import kotlin.time.Instant

expect class NotificationManager {
    fun showNotification(
        title: String,
        description: String,
        soundType: NotificationSoundType = NotificationSoundType.DEFAULT
    )

    @OptIn(ExperimentalTime::class)
    fun scheduleNotification(
        id: String,
        triggerAt: Instant,
        title: String,
        description: String,
        soundType: NotificationSoundType
    )

    fun cancelScheduledNotification(id: String)
}

enum class NotificationSoundType {
    DEFAULT,
    ADHAN,
    IQAMA
}
