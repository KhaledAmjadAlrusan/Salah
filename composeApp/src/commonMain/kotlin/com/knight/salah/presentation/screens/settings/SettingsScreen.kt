package com.knight.salah.presentation.screens.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.BrightnessMedium
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.LocationSearching
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Speaker
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.VolumeDown
import androidx.compose.material.icons.filled.VolumeUp
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import com.knight.salah.presentation.components.SettingsItem
import com.knight.salah.presentation.components.SettingsSection
import com.knight.salah.presentation.components.SettingsSwitchItem
import com.knight.salah.presentation.screens.settings.viewmodel.SettingsViewModel
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = koinViewModel(),
    onBackClick: () -> Unit = {}
) {
    val state by viewModel.stateFlow.collectAsState()

    var locationEnabled by remember { mutableStateOf(true) }
    var darkModeEnabled by remember { mutableStateOf(false) }
    var soundEnabled by remember { mutableStateOf(true) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Settings",
                        color = Color.White,
                        fontWeight = FontWeight.Medium
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        SettingsContent(
            modifier = Modifier.padding(paddingValues),
            state = state,
            notificationsEnabled = viewModel::setNotificationEnabled,
            location = locationEnabled,
            adhanSoundEnabled = viewModel::setAthanSoundEnabled,
            locationEnabled = { locationEnabled = it },
            darkMode = darkModeEnabled,
            darkModeEnabled = { darkModeEnabled = it },
            showNotification = {
                viewModel.showNotification()
            },
            startAdhan = {
                viewModel.startAdhan()
            },
            iqamaSoundEnabled = viewModel::setIqamaSoundEnabled,
        )
    }
}

@Composable
private fun SettingsContent(
    modifier: Modifier = Modifier,
    state: SettingsState,
    notificationsEnabled: (Boolean) -> Unit,
    adhanSoundEnabled: (Boolean) -> Unit,
    iqamaSoundEnabled: (Boolean) -> Unit,
    location: Boolean,
    locationEnabled: (Boolean) -> Unit,
    darkMode: Boolean,
    darkModeEnabled: (Boolean) -> Unit,
    showNotification: () -> Unit,
    startAdhan: () -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
    ) {
        SettingsSection(title = "Prayer Settings") {
            SettingsSwitchItem(
                icon = Icons.Default.Notifications,
                title = "Prayer Notifications",
                subtitle = "Get notified before prayer times",
                isChecked = state.notificationEnabled,
                onCheckedChange = notificationsEnabled
            )

            SettingsSwitchItem(
                icon = Icons.Default.VolumeUp,
                title = "Adhan Sound",
                subtitle = "Play adhan sound for prayers",
                isChecked = state.adhanSoundEnabled,
                onCheckedChange = adhanSoundEnabled,
                enabled = state.notificationEnabled
            )

            SettingsSwitchItem(
                icon = Icons.Default.VolumeDown,
                title = "Iqama Sound",
                subtitle = "Play iqama sound for prayers",
                isChecked = state.iqamaSoundEnabled,
                onCheckedChange = iqamaSoundEnabled,
                enabled = state.notificationEnabled
            )

            SettingsItem(
                icon = Icons.Default.Language,
                title = "Language",
                subtitle = "English",
                onClick = { }
            )
        }

        SettingsSection(title = "Location") {
            SettingsSwitchItem(
                icon = Icons.Default.LocationOn,
                title = "Auto-location",
                subtitle = "Use device location automatically",
                isChecked = location,
                onCheckedChange = { locationEnabled(it) }
            )

            SettingsItem(
                icon = Icons.Default.LocationSearching,
                title = "Manual Location",
                subtitle = "Set your location manually",
                onClick = { }
            )
        }

        SettingsSection(title = "Appearance") {
            SettingsSwitchItem(
                icon = Icons.Default.BrightnessMedium,
                title = "Dark Mode",
                subtitle = "Switch to dark theme",
                isChecked = darkMode,
                onCheckedChange = { darkModeEnabled(it) }
            )
        }

        SettingsSection(title = "More") {
            SettingsItem(
                icon = Icons.Default.Info,
                title = "About App",
                subtitle = "Version 1.0.0",
                onClick = { }
            )

            SettingsItem(
                icon = Icons.Default.Share,
                title = "Share App",
                subtitle = "Share with friends and family",
                onClick = { }
            )

            SettingsItem(
                icon = Icons.Default.Star,
                title = "Rate App",
                subtitle = "Rate us on Play Store",
                onClick = { }
            )
        }

        SettingsSection(title = "Debug testing") {
            SettingsItem(
                icon = Icons.Default.Speaker,
                title = "Test Notification",
                subtitle = "Instant notification test",
                onClick = { showNotification() }
            )

            SettingsItem(
                icon = Icons.Default.Speaker,
                title = "Test Adhan",
                subtitle = "Instant adhan sound test",
                onClick = { startAdhan() }
            )
        }
    }
}

@Preview
@Composable
fun PreviewSettingsScreen() {
    SettingsScreen()
}
