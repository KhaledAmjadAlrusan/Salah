package com.knight.salah.util

import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.ExperimentalTime


@OptIn(ExperimentalTime::class)
fun currentLocalTime(): LocalTime {
    val now = Clock.System.now()
    val zone = TimeZone.currentSystemDefault()
    return now.toLocalDateTime(zone).time
}

fun String.toLocalTimeOrNull(): LocalTime? =
    runCatching { LocalTime.parse(this) }.getOrNull()
