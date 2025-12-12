package com.knight.salah.presentation.screens.main.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.knight.salah.domain.model.PrayerTime
import com.knight.salah.domain.repoistory.SalahRepository
import com.knight.salah.platform.NotificationManager
import com.knight.salah.presentation.screens.main.viewmodel.state.PrayerState
import com.knight.salah.presentation.screens.main.viewmodel.state.buildTodayLabel
import com.knight.salah.presentation.screens.main.viewmodel.state.nextSwitchInstant
import com.knight.salah.presentation.screens.main.viewmodel.state.schedulePrayerNotifications
import com.knight.salah.presentation.screens.main.viewmodel.state.toPrayerRowsWithNext
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.datetime.TimeZone
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

class MainPrayerViewModel(
    private val repository: SalahRepository,
    private val notificationManager: NotificationManager
) : ViewModel() {

    private val _prayerState = MutableStateFlow(PrayerState())
    val prayerState = _prayerState.asStateFlow()

    private var currentPrayerTime: PrayerTime? = null
    private var watcherJob: Job? = null

    init {
        buildDate()
        viewModelScope.launch {
            getPrayerTime()
        }
    }

    @OptIn(ExperimentalTime::class)
    suspend fun getPrayerTime() {
        updateLoading(true)

        val prayerTime = repository.getPrayers()
        currentPrayerTime = prayerTime

        // schedule for a week (7 days) as you had
        prayerTime?.schedulePrayerNotifications(notificationManager, daysToSchedule = 7)

        _prayerState.update { state ->
            state.copy(
                rows = prayerTime?.toPrayerRowsWithNext() ?: emptyList(),
                isLoading = false
            )
        }

        restartWatcher()
    }

    @OptIn(ExperimentalTime::class)
    private fun restartWatcher() {
        watcherJob?.cancel()
        val prayerTime = currentPrayerTime ?: return

        watcherJob = viewModelScope.launch {
            val zone = TimeZone.currentSystemDefault()

            while (isActive) {
                val now = Clock.System.now()

                // 1) recompute rows for current time
                val rowsNow = prayerTime.toPrayerRowsWithNext(now, zone)
                _prayerState.update { it.copy(rows = rowsNow) }

                // 2) when does "next prayer" switch?
                val next = prayerTime.nextSwitchInstant(now, zone) ?: break

                val delayMs = (next - now).inWholeMilliseconds
                    .coerceAtLeast(0L)

                delay(delayMs)
            }
        }
    }

    @OptIn(ExperimentalTime::class)
    private fun buildDate(){
        _prayerState.update { state ->
            state.copy(
                date = buildTodayLabel()
            )
        }
    }
    fun updateLoading(isLoading: Boolean) {
        _prayerState.update { state ->
            state.copy(isLoading = isLoading)
        }
    }
}