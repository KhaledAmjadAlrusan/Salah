package com.knight.salah

import android.app.Application
import android.content.Context

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
        AppInitializer.onApplicationStart()
    }
}
