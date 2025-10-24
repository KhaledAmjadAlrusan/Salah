package com.knight.salah.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.knight.salah.domain.PrayerTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.format
import kotlinx.datetime.format.FormatStringsInDatetimeFormats
import kotlinx.datetime.format.Padding
import kotlinx.datetime.format.char
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(FormatStringsInDatetimeFormats::class)
@Composable
fun PrayerTimeCard(
    modifier: Modifier = Modifier,
    prayer: PrayerTime,
) {
    // No opt-in needed; not using byUnicodePattern
    val timeFmt = remember {
        LocalTime.Format {
            amPmHour(Padding.NONE)   // 1..12, no leading zero
            char(':'); minute()      // zero-padded by default
            char(' '); amPmMarker(
            am = "AM",
            pm = "PM"
        )
        }
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(80.dp)
            .padding(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (prayer.isNextPrayer) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onPrimary
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = prayer.name,
                    style = MaterialTheme.typography.bodyLarge,
                    color = if (prayer.isNextPrayer) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Medium,
                    fontSize = 18.sp
                )
                if (prayer.isNextPrayer) {
                    Text(
                        text = "Next Prayer",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.surface
                    )
                }
            }

            Text(
                text = prayer.time?.format(timeFmt) ?: "--:--",
                style = MaterialTheme.typography.headlineSmall,
                color = if (prayer.isNextPrayer) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
        }
    }
}


@Preview
@Composable
fun PreviewPrayerTimeCard() {
    PrayerTimeCard(
        prayer = PrayerTime("Fajr", LocalTime(hour = 5, minute = 30))
    )
}
