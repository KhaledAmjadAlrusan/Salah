package com.knight.salah.notificationService


expect class NotificationService {

    fun showNotification(
        title: String,
        message: String?
    )
}