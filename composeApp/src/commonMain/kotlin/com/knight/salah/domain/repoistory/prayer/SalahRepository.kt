package com.knight.salah.domain.repoistory.prayer

import com.knight.salah.data.datastore.mosue.MosqueDataSource
import com.knight.salah.data.local.PrayerTimeDao
import com.knight.salah.data.prayer.PrayerApi
import com.knight.salah.domain.model.local.prayer.PrayerTimeEntity
import com.knight.salah.domain.model.mapper.asDomain
import com.knight.salah.domain.model.mapper.asEntity
import com.knight.salah.domain.model.remote.pryaer.DailyPrayerTime
import com.knight.salah.domain.model.remote.pryaer.DatedPrayerTime
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.todayIn
import kotlin.time.Clock
import kotlin.time.Duration.Companion.hours
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalCoroutinesApi::class, ExperimentalTime::class)
class SalahRepository(
    private val prayerApi: PrayerApi,
    private val dataStore: MosqueDataSource,
    private val prayerTimeDao: PrayerTimeDao
) {
    fun observePrayer(
        prayerDate: String = getTodaysDate()
    ): Flow<DailyPrayerTime?> {
        return dataStore.getSelectedMosqueId()
            .distinctUntilChanged()
            .flatMapLatest { organizationId ->
                if (organizationId == null) {
                    flowOf(null)
                } else {
                    prayerTimeDao.observePrayer(
                        organizationId = organizationId,
                        prayerDate = prayerDate
                    ).map { it?.asDomain() }
                }
            }
    }

    suspend fun ensurePrayerCached(
        organizationId: String,
        prayerDate: String,
        forceRefresh: Boolean = false
    ): DailyPrayerTime? {
        val cached = prayerTimeDao.getPrayer(organizationId, prayerDate)

        if (!forceRefresh && cached != null && !isStale(cached, prayerDate)) {
            return cached.asDomain()
        }

        val remote = runCatching {
            prayerApi.getPrayerTime(
                organizationId = organizationId,
                prayerDate = prayerDate
            )
        }.getOrNull()

        if (remote != null) {
            prayerTimeDao.upsert(remote.asEntity())
            return remote
        }

        return cached?.asDomain()
    }

    suspend fun ensurePrayerRangeCached(
        daysCount: Int,
        startDate: LocalDate = Clock.System.todayIn(TimeZone.currentSystemDefault()),
        forceRefresh: Boolean = false
    ): List<DatedPrayerTime> {
        require(daysCount > 0)

        val organizationId = dataStore.getSelectedMosqueId().first() ?: return emptyList()
        val dates = (0 until daysCount).map { startDate.plus(it, DateTimeUnit.DAY) }

        dates.forEach { date ->
            ensurePrayerCached(
                organizationId = organizationId,
                prayerDate = date.toString(),
                forceRefresh = forceRefresh
            )
        }

        val start = dates.first().toString()
        val end = dates.last().toString()

        return prayerTimeDao.getPrayersInRange(
            organizationId = organizationId,
            startDate = start,
            endDate = end
        ).map { entity ->
            DatedPrayerTime(
                date = LocalDate.parse(entity.prayerDate),
                prayer = entity.asDomain()
            )
        }
    }

    private fun isStale(
        cached: PrayerTimeEntity,
        requestedDate: String
    ): Boolean {
        val nowMs = Clock.System.now().toEpochMilliseconds()
        val ageMs = nowMs - cached.fetchedAtEpochMs

        return if (requestedDate == getTodaysDate()) {
            ageMs > 6.hours.inWholeMilliseconds
        } else {
            ageMs > 24.hours.inWholeMilliseconds
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