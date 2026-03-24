package com.knight.salah.presentation.screens.main.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.knight.salah.domain.model.pryaer.DailyPrayerTime
import com.knight.salah.domain.repoistory.prayer.RefreshPrayerUseCase
import com.knight.salah.domain.repoistory.prayer.SalahRepository
import com.knight.salah.platform.NotificationManager
import com.knight.salah.presentation.screens.main.data.PrayerState
import com.knight.salah.presentation.screens.main.data.buildTodayLabel
import com.knight.salah.presentation.screens.main.data.nextSwitchInstant
import com.knight.salah.presentation.screens.main.data.toPrayerRowsWithNext
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.datetime.TimeZone
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

class MainPrayerViewModel(
    private val repository: SalahRepository,
    private val notificationManager: NotificationManager,
    private val refreshPrayerUseCase: RefreshPrayerUseCase
) : ViewModel() {

    private val _prayerState = MutableStateFlow(PrayerState())
    val prayerState = _prayerState.asStateFlow()

    private var currentPrayerTime: DailyPrayerTime? = null
    private var watcherJob: Job? = null

    init {
        updateLoading(true)
        observePrayerTime()
    }

    @OptIn(ExperimentalTime::class)
    private fun observePrayerTime() {
        viewModelScope.launch {
            repository.getPrayers()
                .collectLatest { prayerTime ->
                    currentPrayerTime = prayerTime

                    _prayerState.update { state ->
                        state.copy(
                            rows = prayerTime?.toPrayerRowsWithNext() ?: emptyList(),
                            mosqueName = prayerTime?.organizationId ?: "Select Mosque",
                            date = buildTodayLabel(),
                            isLoading = false
                        )
                    }

                    restartWatcher(prayerTime)

                    if (prayerTime != null) {
                        refreshPrayerUseCase.suspendedRefreshPrayerTimesAndSchedule(
                            daysToSchedule = 7
                        )
                    }
                }
        }
    }

    @OptIn(ExperimentalTime::class)
    private fun restartWatcher(prayerTime: DailyPrayerTime?) {
        watcherJob?.cancel()
        prayerTime ?: return

        watcherJob = viewModelScope.launch {
            val zone = TimeZone.currentSystemDefault()

            while (isActive) {
                val now = Clock.System.now()

                _prayerState.update {
                    it.copy(
                        rows = prayerTime.toPrayerRowsWithNext(now, zone),
                        date = buildTodayLabel(now, zone)
                    )
                }

                val next = prayerTime.nextSwitchInstant(now, zone) ?: break
                val delayMs = (next - now).inWholeMilliseconds.coerceAtLeast(0L)
                delay(delayMs)
            }
        }
    }

    fun updateLoading(isLoading: Boolean) {
        _prayerState.update { state ->
            state.copy(isLoading = isLoading)
        }
    }
}