package com.knight.salah.data.prayer

import com.knight.salah.domain.model.pryaer.DailyPrayerTime

interface PrayerApi {
    // Prayer Date formate 2023-03-21
    suspend fun getPrayerTime(organizationId: String, prayerDate: String): DailyPrayerTime?
}