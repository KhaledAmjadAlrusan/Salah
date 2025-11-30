package com.knight.salah.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.knight.salah.domain.PrayerTime
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SalahViewModel(
    private val repository: SalahRepository
) : ViewModel() {

    val _prayerTime = MutableStateFlow<PrayerTime?>(null)
    val prayerTime = _prayerTime.asStateFlow()

    init {
        getPrayerTime()
    }

    fun getPrayerTime() {
        viewModelScope.launch {
            _prayerTime.emit(repository.getPrayers())
        }
    }
}