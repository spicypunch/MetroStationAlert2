package com.jm.metrostationalert.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Star
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(
    val route: String,
    val title: String,
    val icon: ImageVector,
    val selectedIcon: ImageVector
) {
    data object Search : BottomNavItem(
        route = "search",
        title = "검색",
        icon = Icons.Outlined.Search,
        selectedIcon = Icons.Filled.Search
    )
    
    data object Bookmark : BottomNavItem(
        route = "bookmark",
        title = "북마크",
        icon = Icons.Outlined.Star,
        selectedIcon = Icons.Filled.Star
    )
    
    data object Settings : BottomNavItem(
        route = "settings",
        title = "설정",
        icon = Icons.Outlined.Settings,
        selectedIcon = Icons.Filled.Settings
    )
}