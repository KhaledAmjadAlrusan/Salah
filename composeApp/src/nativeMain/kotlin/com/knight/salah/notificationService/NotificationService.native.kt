package com.knight.salah.notificationService

import com.knight.salah.Platform

actual class NotificationService {
    actual fun showNotification(title: String, message: String?) {
    }

    actual fun requestPermission(
        activity: Platform,
        onFinished: (Boolean) -> Unit
    ) {
    }

    actual suspend fun areNotificationsEnabled(): Boolean {
        TODO("Not yet implemented")
    }

}