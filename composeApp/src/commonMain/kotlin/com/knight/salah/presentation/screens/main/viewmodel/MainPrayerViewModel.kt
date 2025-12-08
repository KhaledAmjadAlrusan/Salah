package com.knight.salah.presentation.screens.main.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.knight.salah.domain.repoistory.SalahRepository
import com.knight.salah.platform.NotificationManager
import com.knight.salah.presentation.screens.main.viewmodel.state.PrayerState
import com.knight.salah.presentation.screens.main.viewmodel.state.schedulePrayerNotifications
import com.knight.salah.presentation.screens.main.viewmodel.state.toPrayerRowsWithNext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.ExperimentalTime

class MainPrayerViewModel(
    private val repository: SalahRepository,
    private val notificationManager: NotificationManager
) : ViewModel() {

    val _prayerState = MutableStateFlow(PrayerState())
    val prayerState = _prayerState.asStateFlow()

    init {
        viewModelScope.launch {
            getPrayerTime()
        }
    }

    @OptIn(ExperimentalTime::class)
    suspend fun getPrayerTime() {
        updateLoading(true)
        val prayerTime = repository.getPrayers()
        prayerTime?.schedulePrayerNotifications(notificationManager)

        _prayerState.update { state ->
            state.copy(
                rows = prayerTime?.toPrayerRowsWithNext() ?: emptyList(),
                isLoading = false
            )
        }
    }

    fun updateLoading(isLoading: Boolean) {
        _prayerState.update { state ->
            state.copy(
                isLoading = isLoading
            )
        }
    }
}
