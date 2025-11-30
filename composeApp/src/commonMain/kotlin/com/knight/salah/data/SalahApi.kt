package com.knight.salah.data

import com.knight.salah.domain.PrayerTime

interface SalahApi {
    suspend fun getPrayerTime(): PrayerTime?
}
