package com.knight.salah.domain.model.pryaer


import com.knight.salah.domain.model.Source
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PrayerTime(
    @SerialName("address")
    val address: String,
    @SerialName("id")
    val id: String,
    @SerialName("name")
    val name: String,
    @SerialName("prayers")
    val prayers: Prayers,
    @SerialName("source")
    val source: Source,
    @SerialName("updated_at")
    val updatedAt: String
)