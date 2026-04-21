package com.knight.salah.presentation.navigation

import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.knight.salah.presentation.screens.main.MainPrayersScreen
import com.knight.salah.presentation.screens.search.SearchScreen
import com.knight.salah.presentation.screens.search.viewmodel.SearchMosqueViewModel
import com.knight.salah.presentation.screens.settings.SettingsScreen
import org.koin.compose.viewmodel.koinViewModel

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
        mainScreenEntry(navController)
        searchScreenEntry(navController)
        settingScreenEntry(navController)
    }
    LaunchedEffect(navController) {
        onNavHostReady(navController)
    }
}

private fun NavGraphBuilder.mainScreenEntry(navController: NavController) {
    composable<MainPrayerScreen> {
        MainPrayersScreen(
            onSearchClick = { navController.navigate(MenuSearchScreen) },
            onSettingsClick = { navController.navigate(MenuSettingsScreen) },
        )
    }
}

private fun NavGraphBuilder.settingScreenEntry(navController: NavController) {
    composable<MenuSettingsScreen> {
        SettingsScreen(
            onBackClick = { navController.navigateUp() }
        )
    }
}

private fun NavGraphBuilder.searchScreenEntry(navController: NavController) {
    composable<MenuSearchScreen> {
        val viewmodel = koinViewModel<SearchMosqueViewModel>()
        val state by viewmodel.prayerState.collectAsStateWithLifecycle()
        SearchScreen(
            state = state,
            onEvent = viewmodel::onEvent,
            onBackClick = { navController.navigateUp() },
        )
    }
}
