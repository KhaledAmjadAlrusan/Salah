package com.knight.salah.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.knight.salah.domain.model.local.prayer.PrayerTimeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PrayerTimeDao {

    @Query(
        """
        SELECT * FROM prayer_times
        WHERE organization_id = :organizationId
        AND prayer_date = :prayerDate
        LIMIT 1
    """
    )
    fun observePrayer(
        organizationId: String,
        prayerDate: String
    ): Flow<PrayerTimeEntity?>

    @Query(
        """
        SELECT * FROM prayer_times
        WHERE organization_id = :organizationId
        AND prayer_date = :prayerDate
        LIMIT 1
    """
    )
    suspend fun getPrayer(
        organizationId: String,
        prayerDate: String
    ): PrayerTimeEntity?

    @Query(
        """
        SELECT * FROM prayer_times
        WHERE organization_id = :organizationId
        AND prayer_date BETWEEN :startDate AND :endDate
        ORDER BY prayer_date ASC
    """
    )
    suspend fun getPrayersInRange(
        organizationId: String,
        startDate: String,
        endDate: String
    ): List<PrayerTimeEntity>

    @Upsert
    suspend fun upsert(item: PrayerTimeEntity)

    @Upsert
    suspend fun upsertAll(items: List<PrayerTimeEntity>)
}