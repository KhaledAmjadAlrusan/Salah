package com.knight.salah.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.knight.salah.domain.PrayerTime
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class SalahViewModel(
    private val repository: SalahRepository
) : ViewModel() {

    val prayerTime = MutableStateFlow<PrayerTime?>(null)
        private set

    init {
        getPrayerTime()
    }

    fun getPrayerTime() {
        viewModelScope.launch {
            prayerTime.emit(repository.getPrayers())
        }
    }
}