package com.knight.salah.notificationService

import com.knight.salah.Platform

expect class NotificationService {

    fun showNotification(
        title: String,
        message: String?
    )

    fun requestPermission(
        activity: Platform,
        onFinished: (Boolean) -> Unit
    )

    suspend fun areNotificationsEnabled(): Boolean
}