package com.knight.salah.presentation.screens.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.knight.salah.core.worker.PrayerBgWorker.refreshPrayerUseCase
import com.knight.salah.domain.repoistory.SettingRepository
import com.knight.salah.platform.NotificationManager
import com.knight.salah.platform.NotificationSoundType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class SettingViewModel(
    private val notificationManager: NotificationManager,
    private val repository: SettingRepository


) : ViewModel() {

    private val _stateFlow = MutableStateFlow(SettingState())
    val stateFlow = _stateFlow.asStateFlow()

    init {
        initNotificationObserver()
        initAdhanSoundObserver()
        initIqamaSoundObserver()
    }

    fun showNotification() {
        notificationManager.showNotification(
            title = "Test Notification",
            description = "This is an instant test notification"
        )
    }

    fun startAdhan() {
        notificationManager.showNotification(
            title = "Test Adhan",
            description = "Testing Adhan sound instantly",
            soundType = NotificationSoundType.ADHAN
        )
    }
    fun setNotificationEnabled(enabled: Boolean) {
        viewModelScope.launch {
            repository.setNotificationEnabled(enabled)
            // Refresh prayer times to apply the new setting
            refreshPrayerUseCase.suspendedRefreshPrayerTimesAndSchedule()
        }
    }

    fun setAthanSoundEnabled(enabled: Boolean) {
        viewModelScope.launch {
            repository.setAthanSoundEnabled(enabled)
            // Refresh prayer times to apply the new setting
            refreshPrayerUseCase.suspendedRefreshPrayerTimesAndSchedule()
        }
    }

    fun setIqamaSoundEnabled(enabled: Boolean) {
        viewModelScope.launch {
            repository.setIqamaSoundEnabled(enabled)
            // Refresh prayer times to apply the new setting
            refreshPrayerUseCase.suspendedRefreshPrayerTimesAndSchedule()
        }
    }

    private fun initNotificationObserver() {
        viewModelScope.launch {
            repository.getNotificationEnabled().collect {
                _stateFlow.value = _stateFlow.value.copy(
                    notificationEnabled = it
                )
            }
        }
    }

    private fun initAdhanSoundObserver() {
        viewModelScope.launch {
            repository.getAthanSoundEnabled().collect {
                _stateFlow.value = _stateFlow.value.copy(
                    adhanSoundEnabled = it
                )
            }
        }
    }

    private fun initIqamaSoundObserver() {
        viewModelScope.launch {
            repository.getIqamaSoundEnabled().collect {
                _stateFlow.value = _stateFlow.value.copy(
                    iqamaSoundEnabled = it
                )
            }
        }
    }
}

data class SettingState(
    val notificationEnabled: Boolean = false,
    val adhanSoundEnabled: Boolean = false,
    val iqamaSoundEnabled: Boolean = false
)
