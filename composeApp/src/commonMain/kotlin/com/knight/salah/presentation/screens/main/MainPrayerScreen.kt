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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.knight.salah.presentation.components.PrayerTimeCard
import com.knight.salah.presentation.screens.main.viewmodel.MainPrayerViewModel
import com.knight.salah.presentation.screens.main.viewmodel.state.PrayerRow
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainPrayersScreen(
    onSearchClick: () -> Unit,
    onSettingsClick: () -> Unit,
    viewModel: MainPrayerViewModel = koinViewModel()
) {
    val state by viewModel.prayerState.collectAsState()

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
            date = state.date,
            prayers = state.rows,
        )
    }
}

@Composable
private fun MainPrayersContent(
    modifier: Modifier = Modifier,
    date:String,
    prayers: List<PrayerRow>,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Text(
            text = date,
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
            items(prayers) { prayer ->
                PrayerTimeCard(prayer = prayer)
            }
        }
    }
}
