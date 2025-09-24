package com.jm.metrostationalert

import android.Manifest
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
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
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission
import com.jm.metrostationalert.navigation.BottomNavItem
import com.jm.metrostationalert.service.LocationService
import dagger.hilt.android.AndroidEntryPoint
import kr.jm.common_ui.theme.bgColor
import kr.jm.feature_bookmark.bookmarkScreen
import kr.jm.feature_search.searchRoute
import kr.jm.feature_search.searchScreen
import kr.jm.feature_settings.settingsScreen

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        requestPermission(this)
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
                .background(bgColor)
        ) {
            NavHost(
                navController = navController,
                startDestination = searchRoute
            ) {
                searchScreen()
                bookmarkScreen()
                settingsScreen()
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
        modifier = modifier,
        containerColor = bgColor
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

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
fun requestPermission(context: Context) {
    TedPermission.create()
        .setPermissionListener(object : PermissionListener {
            override fun onPermissionGranted() {
                val intent = Intent(context, LocationService::class.java)
                context.startForegroundService(intent)
            }

            override fun onPermissionDenied(deniedPermissions: List<String>) {
                Toast.makeText(
                    context,
                    "권한을 허가해주세요.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
        .setDeniedMessage("권한을 허용해주세요. [설정] > [앱 및 알림] > [고급] > [앱 권한]")
        .setPermissions(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.POST_NOTIFICATIONS,
        )
        .check()
}