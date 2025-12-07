package com.knight.salah.data

import com.knight.salah.domain.model.PrayerTime

interface SalahApi {
    suspend fun getPrayerTime(): PrayerTime?
}
