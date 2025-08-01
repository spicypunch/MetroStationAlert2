package kr.jm.feature_bookmark

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

const val bookmarkRoute = "bookmark"

fun NavGraphBuilder.bookmarkScreen() {
    composable(route = bookmarkRoute) {
        BookmarkScreen()
    }
}