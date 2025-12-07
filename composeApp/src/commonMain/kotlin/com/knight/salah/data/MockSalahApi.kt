package com.knight.salah.data

import com.knight.salah.domain.model.DailyPrayer
import com.knight.salah.domain.model.JumuahPrayer
import com.knight.salah.domain.model.PrayerTime
import com.knight.salah.domain.model.Prayers
import com.knight.salah.domain.model.Source

class MockSalahApi : SalahApi {
    override suspend fun getPrayerTime(): PrayerTime? {
        return getMockPrayerTime()
    }
}

private fun getMockPrayerTime(): PrayerTime {
    return PrayerTime(
        id = "mac-vancouver",
        name = "MAC Centre Vancouver",
        address = "2122 Kingsway, Vancouver, BC",
        source = Source(
            type = "html",
            url = "https://centres.macnet.ca/macvancouvercentre/",
            connector = "macnet"
        ),
        updatedAt = "2025-12-07T20:36:32.927174Z",
        prayers = Prayers(
            fajr = DailyPrayer(athan = "06:02", iqama = "06:30"),
            sunrise = "07:51",
            dhuhr = DailyPrayer(athan = "12:06", iqama = "12:45"),
            asr = DailyPrayer(athan = "14:00", iqama = "15:00"),
            maghrib = DailyPrayer(athan = "16:15", iqama = "16:20"),
            isha = DailyPrayer(athan = "18:00", iqama = "20:00"),
            jumuahPrayer = listOf(
                JumuahPrayer(khutbah = "12:30", iqama = "13:00")
            )
        )
    )
}
