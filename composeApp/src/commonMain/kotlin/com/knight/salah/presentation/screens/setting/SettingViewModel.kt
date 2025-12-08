package com.knight.salah.presentation.screens.setting

import androidx.lifecycle.ViewModel
import com.knight.salah.platform.NotificationManager

class SettingViewModel(
    private val notificationManager: NotificationManager
) : ViewModel() {

    fun showNotification(){
        notificationManager.showNotification(
            title = "Title from KMPNotifier",
            description = "Body message from KMPNotifier"
        )
    }
}
