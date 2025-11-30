package com.knight.salah.data

import com.knight.salah.domain.PrayerTime

class SalahRepository(
    private val salahApi: SalahApi
) {
    suspend fun getPrayers(): PrayerTime?{
        return salahApi.getPrayerTime()
    }
}