package com.knight.salah.domain.repoistory.prayer

import com.knight.salah.data.datastore.mosue.MosqueDataSource
import com.knight.salah.data.prayer.PrayerApi
import com.knight.salah.domain.model.pryaer.DailyPrayerTime
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
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
            if (organizationId != null) {
                prayerApi.getPrayerTime(
                    organizationId = organizationId,
                    prayerDate = prayerDate
                )
            } else {
                null
            }
        }
    }

    @OptIn(ExperimentalTime::class, ExperimentalCoroutinesApi::class)
    suspend fun getPrayers(
        daysCount: Int,
        startDate: LocalDate = Clock.System.todayIn(TimeZone.currentSystemDefault())
    ): List<DailyPrayerTime> {
        require(daysCount > 0)

        val organizationId = dataStore.getSelectedMosqueId().first() ?: return emptyList()
        return coroutineScope {
            (0 until daysCount)
                .map { offset ->
                    async {
                        val date = startDate.plus(offset, DateTimeUnit.DAY)
                        prayerApi.getPrayerTime(
                            organizationId = organizationId,
                            prayerDate = date.toString()
                        )
                    }
                }
                .awaitAll()
                .filterNotNull()
                .sortedBy { it.prayerDate }
        }
    }
}

@OptIn(ExperimentalTime::class)
private fun getTodaysDate(): String {
    val today = Clock.System.now()
        .toLocalDateTime(TimeZone.currentSystemDefault())
        .date

    return today.toString() // format: YYYY-MM-DD
}