package com.knight.salah.presentation.screens.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.knight.salah.domain.repoistory.SettingRepository
import com.knight.salah.platform.NotificationManager
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
    }

    fun showNotification() {
        notificationManager.showNotification(
            title = "Title from KMPNotifier",
            description = "Body message from KMPNotifier"
        )
    }

    fun setNotificationEnabled(enabled: Boolean) {
        viewModelScope.launch {
            repository.setNotificationEnabled(enabled)
        }
    }

    fun initNotificationObserver() {
        viewModelScope.launch {
            repository.getNotificationEnabled().collect {
                _stateFlow.value = _stateFlow.value.copy(
                    notificationEnabled = it
                )
            }
        }
    }
}

data class SettingState(
    val notificationEnabled: Boolean = false
)