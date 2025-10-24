package com.knight.salah

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform

expect fun isSystemInDarkMode(): Boolean
