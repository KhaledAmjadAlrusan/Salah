package com.knight.salah.presentation.screens.settings

data class SettingsState(
    val notificationEnabled: Boolean = false,
    val adhanSoundEnabled: Boolean = false,
    val iqamaSoundEnabled: Boolean = false
)
