package com.knight.salah.domain.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Source(
    @SerialName("connector")
    val connector: String,
    @SerialName("type")
    val type: String,
    @SerialName("url")
    val url: String
)