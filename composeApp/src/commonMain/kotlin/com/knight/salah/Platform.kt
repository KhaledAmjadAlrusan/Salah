package com.knight.salah

import androidx.room.RoomDatabase
import com.knight.salah.data.local.SalahDatabase
import org.koin.core.module.Module

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform

expect fun isSystemInDarkMode(): Boolean
expect fun onApplicationStartPlatformSpecific()

expect fun platformModule(): Module

expect fun getSalahDatabaseBuilder(): RoomDatabase.Builder<SalahDatabase>
