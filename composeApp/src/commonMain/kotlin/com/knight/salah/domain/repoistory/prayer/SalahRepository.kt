package com.knight.salah.domain.repoistory.prayer

import com.knight.salah.data.prayer.PrayerApi
import com.knight.salah.domain.model.pryaer.PrayerTime

class SalahRepository(
    private val prayerApi: PrayerApi
) {
    suspend fun getPrayers(): PrayerTime? {
        return prayerApi.getPrayerTime()
    }
}