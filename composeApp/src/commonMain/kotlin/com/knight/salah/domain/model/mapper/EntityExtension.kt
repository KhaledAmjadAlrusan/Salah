package com.knight.salah.domain.model.mapper

import com.knight.salah.domain.model.local.prayer.PrayerTimeEntity
import com.knight.salah.domain.model.remote.prayer.DailyPrayerTime
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
fun DailyPrayerTime.asEntity(
    fetchedAtEpochMs: Long = Clock.System.now().toEpochMilliseconds()
): PrayerTimeEntity {
    return PrayerTimeEntity(
        organizationId = organizationId,
        prayerDate = prayerDate,
        asrAzan = asrAzan,
        asrIqamah = asrIqamah,
        createdAt = createdAt,
        dhuhrAzan = dhuhrAzan,
        dhuhrIqamah = dhuhrIqamah,
        fajrAzan = fajrAzan,
        fajrIqamah = fajrIqamah,
        id = id,
        ishaAzan = ishaAzan,
        ishaIqamah = ishaIqamah,
        jumahTime1 = jumahTime1,
        jumahTime2 = jumahTime2,
        jumahTime3 = jumahTime3,
        maghribAzan = maghribAzan,
        maghribIqamah = maghribIqamah,
        sunrise = sunrise,
        tmrwFajrAzan = tmrwFajrAzan,
        tmrwFajrIqamah = tmrwFajrIqamah,
        zawal = zawal,
        fetchedAtEpochMs = fetchedAtEpochMs
    )
}

fun PrayerTimeEntity.asDomain(): DailyPrayerTime {
    return DailyPrayerTime(
        asrAzan = asrAzan,
        asrIqamah = asrIqamah,
        createdAt = createdAt,
        dhuhrAzan = dhuhrAzan,
        dhuhrIqamah = dhuhrIqamah,
        fajrAzan = fajrAzan,
        fajrIqamah = fajrIqamah,
        id = id,
        ishaAzan = ishaAzan,
        ishaIqamah = ishaIqamah,
        jumahTime1 = jumahTime1,
        jumahTime2 = jumahTime2,
        jumahTime3 = jumahTime3,
        maghribAzan = maghribAzan,
        maghribIqamah = maghribIqamah,
        organizationId = organizationId,
        prayerDate = prayerDate,
        sunrise = sunrise,
        tmrwFajrAzan = tmrwFajrAzan,
        tmrwFajrIqamah = tmrwFajrIqamah,
        zawal = zawal
    )
}
