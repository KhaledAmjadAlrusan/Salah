package com.knight.salah.platform

import kotlin.time.ExperimentalTime
import kotlin.time.Instant

expect class NotificationManager {
    fun showNotification(
        title: String,
        description: String
    )

    @OptIn(ExperimentalTime::class)
    fun scheduleNotification(
        id: String,            // unique per event (e.g. "fajr-athan")
        triggerAt: Instant,    // when to fire
        title: String,
        description: String
    )

    fun cancelScheduledNotification(id: String)

}