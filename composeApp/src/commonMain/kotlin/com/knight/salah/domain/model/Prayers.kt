package com.knight.salah.domain.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Prayers(
    @SerialName("asr")
    val asr: DailyPrayer,
    @SerialName("dhuhr")
    val dhuhr: DailyPrayer,
    @SerialName("fajr")
    val fajr: DailyPrayer,
    @SerialName("isha")
    val isha: DailyPrayer,
    @SerialName("jumuah")
    val jumuahPrayer: List<JumuahPrayer>,
    @SerialName("maghrib")
    val maghrib: DailyPrayer,
    @SerialName("sunrise")
    val sunrise: String
)

@Serializable
data class DailyPrayer(
    @SerialName("athan") val athan: String,
    @SerialName("iqama") val iqama: String
)


@Serializable
data class JumuahPrayer(
    @SerialName("iqama")
    val iqama: String,
    @SerialName("khutbah")
    val khutbah: String
)