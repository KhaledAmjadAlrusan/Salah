package com.knight.salah.presentation.navigation

import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.knight.salah.presentation.screens.main.MainPrayersScreen
import com.knight.salah.presentation.screens.search.SearchScreen
import com.knight.salah.presentation.screens.setting.SettingsScreen

@Composable
fun AppNavGraph(
    onNavHostReady: suspend (NavController) -> Unit = {}
) {
    val navController = rememberNavController()
    NavHost(
        modifier = Modifier.systemBarsPadding(),
        navController = navController,
        startDestination = MainPrayerScreen
    ) {
        composable<MainPrayerScreen> {
            MainPrayersScreen(
                { navController.navigate(MenuSearchScreen) },
                { navController.navigate(MenuSettingsScreen) },

                )
        }
        composable<MenuSearchScreen> {
            SearchScreen(
                onBackClick = { navController.navigateUp() },
            )
        }
        composable<MenuSettingsScreen> {

            SettingsScreen(
                onBackClick = { navController.navigateUp() }
            )
        }
    }
    LaunchedEffect(navController) {
        onNavHostReady(navController)
    }
}
