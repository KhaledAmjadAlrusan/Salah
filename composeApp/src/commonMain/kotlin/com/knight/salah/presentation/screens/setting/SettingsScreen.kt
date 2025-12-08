package com.knight.salah.presentation.screens.setting

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
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: SettingViewModel = koinViewModel(),
    onBackClick: () -> Unit = {}
) {
    var notificationsEnabled by remember { mutableStateOf(true) }
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
            notifications = true,
            notificationsEnabled = {
//                when (viewModel.permissionState) {
//                    Granted -> {
//                    }
//
//                    DeniedAlways -> {
//                        controller.openAppSettings()
//                    }
//
//                    else -> {
//                        viewModel.provideOrRequestNotification()
//                    }
//                }
            },
            sound = soundEnabled,
            soundEnabled = { soundEnabled = it },
            location = locationEnabled,
            locationEnabled = { locationEnabled = it },
            darkMode = darkModeEnabled,
            darkModeEnabled = { darkModeEnabled = it },
            showNotification = {
                viewModel.showNotification()
            }
        )
    }
}

@Composable
private fun SettingsContent(
    modifier: Modifier = Modifier,
    notifications: Boolean,
    notificationsEnabled: (Boolean) -> Unit,
    sound: Boolean,
    soundEnabled: (Boolean) -> Unit,
    location: Boolean,
    locationEnabled: (Boolean) -> Unit,
    darkMode: Boolean,
    darkModeEnabled: (Boolean) -> Unit,
    showNotification: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
    ) {
        // Prayer Settings Section
        SettingsSection(title = "Prayer Settings") {
            SettingsSwitchItem(
                icon = Icons.Default.Notifications,
                title = "Prayer Notifications",
                subtitle = "Get notified before prayer times",
                isChecked = notifications,
                onCheckedChange = { notificationsEnabled(it) }
            )

            SettingsSwitchItem(
                icon = Icons.Default.VolumeUp,
                title = "Adhan Sound",
                subtitle = "Play adhan sound for prayers",
                isChecked = sound,
                onCheckedChange = { soundEnabled(it) }
            )

            SettingsItem(
                icon = Icons.Default.Language,
                title = "Language",
                subtitle = "English",
                onClick = { /* Open calculation method dialog */ }
            )
        }

        // Location Settings Section
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
                onClick = { /* Open location selection */ }
            )
        }

        // Appearance Section
        SettingsSection(title = "Appearance") {
            SettingsSwitchItem(
                icon = Icons.Default.BrightnessMedium,
                title = "Dark Mode",
                subtitle = "Switch to dark theme",
                isChecked = darkMode,
                onCheckedChange = { darkModeEnabled(it) }
            )
        }

        // More Section
        SettingsSection(title = "More") {
            SettingsItem(
                icon = Icons.Default.Info,
                title = "About App",
                subtitle = "Version 1.0.0",
                onClick = { /* Open about screen */ }
            )

            SettingsItem(
                icon = Icons.Default.Share,
                title = "Share App",
                subtitle = "Share with friends and family",
                onClick = { /* Share app */ }
            )

            SettingsItem(
                icon = Icons.Default.Star,
                title = "Rate App",
                subtitle = "Rate us on Play Store",
                onClick = { /* Rate app */ }
            )
        }

        // Prayer Settings Section
        SettingsSection(title = "Debug testing") {

            SettingsItem(
                icon = Icons.Default.Speaker,
                title = "Test Notification",
                subtitle = "Debug",
                onClick = {
//                    val notifier = NotifierManager.getLocalNotifier()
//                    notifier.notify {
//                        id= Random.nextInt(0, Int.MAX_VALUE)
//                        title = "Title from KMPNotifier"
//                        body = "Body message from KMPNotifier"
//                        payloadData = mapOf(
//                            com.mmk.kmpnotifier.notification.Notifier.KEY_URL to "https://github.com/mirzemehdi/KMPNotifier/",
//                            "extraKey" to "randomValue"
//                        )
//                        image = NotificationImage.Url("https://github.com/user-attachments/assets/a0f38159-b31d-4a47-97a7-cc230e15d30b")
//                    }

                    showNotification()
                }
            )
        }

    }
}


@Preview
@Composable
fun PreviewSettingsScreen() {
    SettingsScreen()
}