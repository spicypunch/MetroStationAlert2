package kr.jm.feature_settings

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

const val settingsRoute = "settings"

fun NavGraphBuilder.settingsScreen() {
    composable(route = settingsRoute) {
        SettingsScreen()
    }
}