package com.knight.salah.domain.model.remote.prayer


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DailyPrayerTime(
    @SerialName("asr_azan")
    val asrAzan: String,
    @SerialName("asr_iqamah")
    val asrIqamah: String,
    @SerialName("created_at")
    val createdAt: String,
    @SerialName("dhuhr_azan")
    val dhuhrAzan: String,
    @SerialName("dhuhr_iqamah")
    val dhuhrIqamah: String,
    @SerialName("fajr_azan")
    val fajrAzan: String,
    @SerialName("fajr_iqamah")
    val fajrIqamah: String,
    @SerialName("id")
    val id: String,
    @SerialName("isha_azan")
    val ishaAzan: String,
    @SerialName("isha_iqamah")
    val ishaIqamah: String,
    @SerialName("jumah_time_1")
    val jumahTime1: String,
    @SerialName("jumah_time_2")
    val jumahTime2: String?,
    @SerialName("jumah_time_3")
    val jumahTime3: String?,
    @SerialName("maghrib_azan")
    val maghribAzan: String,
    @SerialName("maghrib_iqamah")
    val maghribIqamah: String,
    @SerialName("organization_id")
    val organizationId: String,
    @SerialName("prayer_date")
    val prayerDate: String,
    @SerialName("sunrise")
    val sunrise: String,
    @SerialName("tmrw_fajr_azan")
    val tmrwFajrAzan: String?,
    @SerialName("tmrw_fajr_iqamah")
    val tmrwFajrIqamah: String?,
    @SerialName("zawal")
    val zawal: String?
)
