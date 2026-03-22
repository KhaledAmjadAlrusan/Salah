package com.knight.salah.domain.model.mosque

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AwqatMosque(
    @SerialName("address")
    val address: String,
    @SerialName("city")
    val city: String,
    @SerialName("id")
    val id: String,
    @SerialName("is_active")
    val isActive: Boolean,
    @SerialName("latitude")
    val latitude: Double,
    @SerialName("longitude")
    val longitude: Double,
    @SerialName("name")
    val name: String,
    @SerialName("province_state")
    val provinceState: String,
    @SerialName("type")
    val type: String
)