package com.knight.salah.data.prayer.mock

//import com.knight.salah.data.prayer.PrayerApi
//import com.knight.salah.domain.model.pryaer.DailyPrayer
//import com.knight.salah.domain.model.pryaer.DailyPrayerTime
//import com.knight.salah.domain.model.pryaer.JumuahPrayer
//import com.knight.salah.domain.model.pryaer.Prayers
//
//class MockPrayerApi : PrayerApi {
//    override suspend fun getPrayerTime(): DailyPrayerTime? {
//        return getMockPrayerTime()
//    }
//}
//
//private fun getMockPrayerTime(): PrayerTime {
//    return PrayerTime(
//        id = "mac-vancouver",
//        name = "MAC Centre Vancouver",
//        address = "2122 Kingsway, Vancouver, BC",
//        source = Source(
//            type = "html",
//            url = "https://centres.macnet.ca/macvancouvercentre/",
//            connector = "macnet"
//        ),
//        updatedAt = "2025-12-07T20:36:32.927174Z",
//        prayers = Prayers(
//            fajr = DailyPrayer(athan = "06:02", iqama = "06:30"),
//            sunrise = "07:51",
//            dhuhr = DailyPrayer(athan = "12:06", iqama = "12:45"),
//            asr = DailyPrayer(athan = "14:00", iqama = "15:00"),
//            maghrib = DailyPrayer(athan = "16:15", iqama = "16:20"),
//            isha = DailyPrayer(athan = "18:00", iqama = "20:00"),
//            jumuahPrayer = listOf(
//                JumuahPrayer(khutbah = "12:30", iqama = "13:00")
//            )
//        )
//    )
//}
