package com.knight.salah.data.local

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.knight.salah.domain.model.local.prayer.PrayerTimeEntity
import com.knight.salah.getSalahDatabaseBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO

@Database(
    entities = [PrayerTimeEntity::class],
    version = 1,
    exportSchema = true
)
@ConstructedBy(SalahDatabaseConstructor::class)
abstract class SalahDatabase : RoomDatabase() {
    abstract fun prayerTimeDao(): PrayerTimeDao
}

@Suppress("KotlinNoActualForExpect")
expect object SalahDatabaseConstructor : RoomDatabaseConstructor<SalahDatabase> {
    override fun initialize(): SalahDatabase
}

fun createSalahDatabase(): SalahDatabase {
    return getSalahDatabaseBuilder()
        .setDriver(BundledSQLiteDriver())
        .setQueryCoroutineContext(Dispatchers.IO)
        .build()
}