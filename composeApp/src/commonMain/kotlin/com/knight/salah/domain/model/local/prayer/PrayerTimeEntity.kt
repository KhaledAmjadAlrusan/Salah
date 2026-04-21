package com.knight.salah.domain.model.local.prayer

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(
    tableName = "prayer_times",
    primaryKeys = ["organization_id", "prayer_date"]
)
data class PrayerTimeEntity(
    @ColumnInfo(name = "organization_id")
    val organizationId: String,
    @ColumnInfo(name = "prayer_date")
    val prayerDate: String,

    @ColumnInfo(name = "asr_azan")
    val asrAzan: String,
    @ColumnInfo(name = "asr_iqamah")
    val asrIqamah: String,
    @ColumnInfo(name = "created_at")
    val createdAt: String,
    @ColumnInfo(name = "dhuhr_azan")
    val dhuhrAzan: String,
    @ColumnInfo(name = "dhuhr_iqamah")
    val dhuhrIqamah: String,
    @ColumnInfo(name = "fajr_azan")
    val fajrAzan: String,
    @ColumnInfo(name = "fajr_iqamah")
    val fajrIqamah: String,
    @ColumnInfo(name = "id")
    val id: String,
    @ColumnInfo(name = "isha_azan")
    val ishaAzan: String,
    @ColumnInfo(name = "isha_iqamah")
    val ishaIqamah: String,
    @ColumnInfo(name = "jumah_time_1")
    val jumahTime1: String,
    @ColumnInfo(name = "jumah_time_2")
    val jumahTime2: String?,
    @ColumnInfo(name = "jumah_time_3")
    val jumahTime3: String?,
    @ColumnInfo(name = "maghrib_azan")
    val maghribAzan: String,
    @ColumnInfo(name = "maghrib_iqamah")
    val maghribIqamah: String,
    @ColumnInfo(name = "sunrise")
    val sunrise: String,
    @ColumnInfo(name = "tmrw_fajr_azan")
    val tmrwFajrAzan: String?,
    @ColumnInfo(name = "tmrw_fajr_iqamah")
    val tmrwFajrIqamah: String?,
    @ColumnInfo(name = "zawal")
    val zawal: String?,

    @ColumnInfo(name = "fetched_at_epoch_ms")
    val fetchedAtEpochMs: Long
)