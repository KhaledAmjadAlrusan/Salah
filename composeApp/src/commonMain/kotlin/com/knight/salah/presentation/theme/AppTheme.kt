package com.knight.salah.presentation.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.isSystemInDarkTheme


val Black            = Color(0xFF000000)
val White            = Color(0xFFFFFFFF)
val PrimaryGreen     = Color(0xFF2E7D32)
val LightGreen       = Color(0xFFE8F5E8)
val BackgroundBeige  = Color(0xFFFDF6E3)
val TextDark         = Color(0xFF37474F)
val TextLight        = Color(0xFF78909C)
val AccentGold       = Color(0xFFD4AF37)

val LightColors = lightColorScheme(
    primary              = PrimaryGreen,
    onPrimary            = White,
    primaryContainer     = LightGreen,
    onPrimaryContainer   = TextDark,

    secondary            = AccentGold,
    onSecondary          = Black,
    secondaryContainer   = Color(0xFFFFF3C4),
    onSecondaryContainer = TextDark,

    background           = BackgroundBeige,
    onBackground         = TextDark,

    surface              = White,
    onSurface            = TextDark,

    surfaceVariant       = Color(0xFFE3ECEF),
    onSurfaceVariant     = TextDark,

    outline              = TextLight
)

val DarkColors = darkColorScheme(
    primary              = Color(0xFF58A05C),
    onPrimary            = Black,
    primaryContainer     = Color(0xFF0F2911),
    onPrimaryContainer   = Color(0xFFB8F0BE),

    secondary            = AccentGold,
    onSecondary          = Black,
    secondaryContainer   = Color(0xFF3A300A),
    onSecondaryContainer = Color(0xFFF4E6A6),

    background           = Color(0xFF121212),
    onBackground         = Color(0xFFE6ECEF),

    surface              = Color(0xFF121212),
    onSurface            = Color(0xFFE6ECEF),

    surfaceVariant       = Color(0xFF1E2528),
    onSurfaceVariant     = Color(0xFFCAD5DA),

    outline              = Color(0xFF8FA0A8)
)

enum class ThemeSetting { Light, Dark, System }

@Composable
fun AppTheme(
    setting: ThemeSetting = ThemeSetting.System,
    content: @Composable () -> Unit
) {
    val dark = when (setting) {
        ThemeSetting.Light -> false
        ThemeSetting.Dark  -> true
        ThemeSetting.System -> isSystemInDarkTheme()
    }
    MaterialTheme(
        colorScheme = if (dark) DarkColors else LightColors,
        content = content
    )
}
