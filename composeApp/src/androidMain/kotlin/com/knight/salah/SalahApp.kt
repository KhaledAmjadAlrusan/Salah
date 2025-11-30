package com.knight.salah

import android.app.Application

class SalahApp: Application() {
    override fun onCreate() {
        super.onCreate()
        AppInitializer.onApplicationStart()
    }
}
