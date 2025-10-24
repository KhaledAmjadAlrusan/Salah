package com.knight.salah.presentation.screens.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.knight.salah.domain.PrayerTime
import com.knight.salah.domain.generatePrayerTimes
import com.knight.salah.presentation.components.PrayerTimeCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainPrayersScreen(
    onSearchClick: () -> Unit,
    onSettingsClick: () -> Unit
) {

    val prayerTimes = remember { generatePrayerTimes() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Prayer Times",
                        color = Color.White,
                        fontWeight = FontWeight.Medium
                    )

                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                actions = {
                    // Search Button
                    IconButton(onClick = onSearchClick) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search Mosques",
                            tint = Color.White
                        )
                    }

                    // Settings Button
                    IconButton(onClick = onSettingsClick) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Settings",
                            tint = Color.White
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        MainPrayersContent(
            modifier = Modifier.padding(paddingValues),
            prayerTimes = prayerTimes,
        )
    }
}

@Composable
private fun MainPrayersContent(
    modifier: Modifier = Modifier,
    prayerTimes: List<PrayerTime>,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {

        Text(
            text = "Today, November 15, 2024",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.outline,
            modifier = Modifier
                .padding(12.dp), fontSize = 16.sp
        )

        Text(
            text = "Prayer Times",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(8.dp)
        )

        LazyColumn {
            items(prayerTimes) { prayer ->
                PrayerTimeCard(prayer = prayer)
            }
        }

    }
}
