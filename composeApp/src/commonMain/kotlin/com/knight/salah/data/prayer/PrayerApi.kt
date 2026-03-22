package com.knight.salah.data.prayer

import com.knight.salah.domain.model.pryaer.PrayerTime

interface PrayerApi {
    suspend fun getPrayerTime(): PrayerTime?
}