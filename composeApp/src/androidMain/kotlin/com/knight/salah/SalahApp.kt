package com.knight.salah

import android.app.Application
import android.content.Context
import com.knight.salah.worker.PrayerRefreshWorker

class SalahApp: Application() {
    companion object{
        lateinit var instance: SalahApp
        fun applicationContext(): Context {
            return instance.applicationContext
        }
    }
    override fun onCreate() {
        super.onCreate()
        instance = this
        init()
    }

    private fun init(){
        AppInitializer.onApplicationStart()
        PrayerRefreshWorker.scheduleDailyPrayerRefresh(this)
    }
}
