package com.knight.salah.domain.repoistory.prayer

import com.knight.salah.data.datastore.mosue.MosqueDataSource
import com.knight.salah.data.prayer.PrayerApi
import com.knight.salah.domain.model.remote.pryaer.DailyPrayerTime
import com.knight.salah.domain.model.remote.pryaer.DatedPrayerTime
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.mapLatest
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.todayIn
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

class SalahRepository(
    private val prayerApi: PrayerApi,
    private val dataStore: MosqueDataSource
) {
    suspend fun getPrayers(
        organizationId: String,
        prayerDate: String = getTodaysDate()
    ): DailyPrayerTime? {
        return prayerApi.getPrayerTime(
            organizationId = organizationId,
            prayerDate = prayerDate
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun getPrayers(
        prayerDate: String = getTodaysDate()
    ): Flow<DailyPrayerTime?> {
        return dataStore.getSelectedMosqueId().mapLatest { organizationId ->
            organizationId?.let {
                prayerApi.getPrayerTime(
                    organizationId = it,
                    prayerDate = prayerDate
                )
            }
        }
    }

    @OptIn(ExperimentalTime::class)
    suspend fun getPrayers(
        daysCount: Int,
        startDate: LocalDate = Clock.System.todayIn(TimeZone.currentSystemDefault())
    ): List<DatedPrayerTime> {
        require(daysCount > 0)

        val organizationId = dataStore.getSelectedMosqueId().first() ?: return emptyList()

        return (0 until daysCount).mapNotNull { offset ->
            val date = startDate.plus(offset, DateTimeUnit.DAY)
            val prayer = prayerApi.getPrayerTime(
                organizationId = organizationId,
                prayerDate = date.toString()
            )
            prayer?.let { DatedPrayerTime(date = date, prayer = it) }
        }
    }
}

@OptIn(ExperimentalTime::class)
private fun getTodaysDate(): String {
    return Clock.System.now()
        .toLocalDateTime(TimeZone.currentSystemDefault())
        .date
        .toString() // format: YYYY-MM-DD
}