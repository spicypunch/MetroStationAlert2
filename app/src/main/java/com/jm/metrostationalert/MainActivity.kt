package com.jm.metrostationalert

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.jm.metrostationalert.navigation.BottomNavItem
import kr.jm.feature_search.SearchScreen
import kr.jm.feature_search.searchRoute
import kr.jm.feature_search.searchScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                App()
            }
        }
    }
}

@Composable
fun App() {
    val navController = rememberNavController()
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    Scaffold(
        bottomBar = {
            MyBottomNavigation(
                items = listOf(
                    BottomNavItem.Search,
                    BottomNavItem.Bookmark,
                    BottomNavItem.Settings
                ),
                selectedRoute = currentRoute ?: BottomNavItem.Search.route,
                onItemClick = { route -> navController.navigate(route) }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            NavHost(
                navController = navController,
                startDestination = searchRoute
            ) {
                searchScreen()
            }
        }
    }
}

@Composable
fun MyBottomNavigation(
    items: List<BottomNavItem>,
    selectedRoute: String,
    onItemClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier.background(Color.White)
    ) {
        items.forEach { screen ->
            NavigationBarItem(
                selected = selectedRoute == screen.route,
                onClick = { onItemClick(screen.route) },
                icon = {
                    Icon(
                        imageVector = if (selectedRoute == screen.route) {
                            screen.selectedIcon
                        } else {
                            screen.icon
                        },
                        tint = if (selectedRoute == screen.route) {
                            Color.Black
                        } else {
                            Color.Gray
                        },
                        contentDescription = screen.title
                    )
                },
                label = {
                    Text(text = screen.title)
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color.Black,
                    unselectedIconColor = Color.Gray,
                    selectedTextColor = Color.Black,
                    unselectedTextColor = Color.Gray,
                    indicatorColor = Color.White
                )
            )
        }
    }
}