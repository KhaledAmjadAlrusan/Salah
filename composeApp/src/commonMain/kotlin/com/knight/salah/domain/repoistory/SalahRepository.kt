package com.knight.salah.domain.repoistory

import com.knight.salah.data.SalahApi
import com.knight.salah.domain.model.PrayerTime

class SalahRepository(
    private val salahApi: SalahApi
) {
    suspend fun getPrayers(): PrayerTime?{
        return salahApi.getPrayerTime()
    }
}