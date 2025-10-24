package com.knight.salah

import androidx.compose.runtime.Composable
import com.knight.salah.presentation.navigation.AppNavGraph
import com.knight.salah.presentation.theme.AppTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    AppTheme {
        AppNavGraph()
    }
}

