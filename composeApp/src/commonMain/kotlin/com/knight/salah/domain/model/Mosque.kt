package com.knight.salah.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Mosque(
    val id: String,
    val name: String,
    val location: String,
    val distance: String
)
fun generateMosques(): List<Mosque> {
    return listOf(
        Mosque("1", "Masjid Al-Haram", "Mecca, Saudi Arabia", "0 km"),
        Mosque("2", "Masjid An-Nabawi", "Medina, Saudi Arabia", "0 km"),
        Mosque("3", "Al-Aqsa Mosque", "Jerusalem, Palestine", "0 km"),
        Mosque("4", "Sultan Ahmed Mosque", "Istanbul, Turkey", "2.5 km"),
        Mosque("5", "Sheikh Zayed Mosque", "Abu Dhabi, UAE", "5.2 km")
    )
}