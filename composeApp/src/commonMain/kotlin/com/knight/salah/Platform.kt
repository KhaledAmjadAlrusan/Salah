package com.knight.salah

import org.koin.core.module.Module

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform

expect fun isSystemInDarkMode(): Boolean
expect fun onApplicationStartPlatformSpecific()

expect fun platformModule(): Module