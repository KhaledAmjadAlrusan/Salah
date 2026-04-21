package com.knight.salah.presentation.screens.main.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.knight.salah.domain.model.remote.pryaer.DailyPrayerTime
import com.knight.salah.domain.repoistory.mosque.MosqueRepository
import com.knight.salah.domain.repoistory.prayer.RefreshPrayerUseCase
import com.knight.salah.domain.repoistory.prayer.SalahRepository
import com.knight.salah.presentation.screens.main.data.PrayerState
import com.knight.salah.presentation.screens.main.data.buildTodayLabel
import com.knight.salah.presentation.screens.main.data.nextSwitchInstant
import com.knight.salah.presentation.screens.main.data.toPrayerRowsWithNext
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.datetime.TimeZone
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

class MainPrayerViewModel(
    private val salahRepository: SalahRepository,
    private val mosqueRepository: MosqueRepository,
    private val refreshPrayerUseCase: RefreshPrayerUseCase
) : ViewModel() {

    private val _prayerState = MutableStateFlow(PrayerState())
    val prayerState = _prayerState.asStateFlow()

    private var watcherJob: Job? = null

    init {
        updateLoading(true)
        observeScreenData()
        refreshOnMosqueChange()
    }

    @OptIn(ExperimentalTime::class)
    private fun observeScreenData() {
        viewModelScope.launch {
            combine(
                salahRepository.observePrayer(),
                mosqueRepository.observeSelectedMosque()
            ) { prayerTime, selectedMosque ->
                prayerTime to selectedMosque
            }.collectLatest { (prayerTime, selectedMosque) ->
                _prayerState.update { state ->
                    state.copy(
                        rows = prayerTime?.toPrayerRowsWithNext() ?: emptyList(),
                        mosqueName = selectedMosque?.name ?: "Select Mosque",
                        date = buildTodayLabel(),
                        isLoading = false
                    )
                }

                restartWatcher(prayerTime)
            }
        }
    }

    private fun refreshOnMosqueChange() {
        viewModelScope.launch {
            mosqueRepository.observeSelectedMosqueId()
                .filterNotNull()
                .distinctUntilChanged()
                .collectLatest {
                    updateLoading(true)
                    refreshPrayerUseCase.suspendedRefreshPrayerTimesAndSchedule(
                        daysToSchedule = 7
                    )
                    updateLoading(false)
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
        _prayerState.update { it.copy(isLoading = isLoading) }
    }

    fun onManualRefresh() {
        viewModelScope.launch {
            refreshPrayerUseCase.suspendedRefreshPrayerTimesAndSchedule(
                daysToSchedule = 7,
                forceRefresh = true
            )
        }
    }
}