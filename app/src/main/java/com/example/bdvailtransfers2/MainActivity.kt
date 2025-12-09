package com.example.bdvailtransfers2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.bdvailtransfers2.ui.screens.BookingFormScreen
import com.example.bdvailtransfers2.ui.screens.BookingSuccessScreen
import com.example.bdvailtransfers2.ui.screens.HomeScreen
import com.example.bdvailtransfers2.ui.screens.MyBookingsScreen
import com.example.bdvailtransfers2.ui.screens.RoutesScreen
import com.example.bdvailtransfers2.ui.screens.SettingsScreen
import com.example.bdvailtransfers2.ui.screens.SupportScreen
import com.example.bdvailtransfers2.ui.theme.BDVailTransfersTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BDVailTransfersTheme {
                BDVailTransfersApp()
            }
        }
    }
}

/** Константы маршрутов навигации */
object NavRoutes {
    const val HOME = "home"
    const val ROUTES = "routes"
    const val BOOKING_FORM = "booking_form"
    const val BOOKING_SUCCESS = "booking_success"
    const val MY_BOOKINGS = "my_bookings"
    const val SUPPORT = "support"
    const val SETTINGS = "settings"
    const val PROFILE = "profile"
}

/** Элемент нижней навигации */
data class BottomNavItem(
    val route: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val label: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BDVailTransfersApp() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination: NavDestination? = navBackStackEntry?.destination
    val currentRoute: String? = currentDestination?.route

    val bottomItems = listOf(
        BottomNavItem(
            route = NavRoutes.HOME,
            icon = Icons.Filled.Home,
            label = "Home"
        ),
        BottomNavItem(
            route = NavRoutes.MY_BOOKINGS,
            icon = Icons.Filled.List,
            label = "Trips"
        ),
        BottomNavItem(
            route = NavRoutes.PROFILE,
            icon = Icons.Filled.Person,
            label = "Profile"
        ),
        BottomNavItem(
            route = NavRoutes.SETTINGS,
            icon = Icons.Filled.Menu,
            label = "Menu"
        )
    )

    val bottomDestinations = bottomItems.map { it.route }.toSet()
    val isBottomDestination = currentRoute == null || bottomDestinations.contains(currentRoute)

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                navigationIcon = {
                    if (!isBottomDestination && currentRoute != null) {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                imageVector = Icons.Filled.ArrowBack,
                                contentDescription = "Back"
                            )
                        }
                    }
                },
                title = {
                    val title = when {
                        currentRoute == null -> "BDVail"
                        currentRoute.startsWith(NavRoutes.BOOKING_FORM) -> "New Booking"
                        currentRoute == NavRoutes.BOOKING_SUCCESS -> "Booking confirmed"
                        currentRoute == NavRoutes.ROUTES -> "Routes"
                        currentRoute == NavRoutes.MY_BOOKINGS -> "My Trips"
                        currentRoute == NavRoutes.PROFILE -> "Profile"
                        currentRoute == NavRoutes.SUPPORT -> "Support"
                        currentRoute == NavRoutes.SETTINGS -> "Menu"
                        currentRoute == NavRoutes.HOME -> "Home"
                        else -> "BDVail"
                    }
                    Text(text = title)
                }
            )
        },
        floatingActionButton = {
            if (isBottomDestination) {
                FloatingActionButton(
                    onClick = {
                        navController.navigate(NavRoutes.BOOKING_FORM) {
                            launchSingleTop = true
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = "New booking"
                    )
                }
            }
        },
        floatingActionButtonPosition = FabPosition.Center,
        bottomBar = {
            if (isBottomDestination) {
                NavigationBar {
                    bottomItems.forEach { item ->
                        val selected =
                            currentDestination?.hierarchy?.any { it.route == item.route } == true

                        NavigationBarItem(
                            selected = selected,
                            onClick = {
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.startDestinationId) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = {
                                Icon(
                                    imageVector = item.icon,
                                    contentDescription = item.label
                                )
                            },
                            label = { Text(text = item.label) }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = NavRoutes.HOME,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(NavRoutes.HOME) {
                HomeScreen(navController)
            }
            composable(NavRoutes.ROUTES) {
                RoutesScreen(navController)
            }
            composable(NavRoutes.BOOKING_FORM) {
                BookingFormScreen(navController = navController, routeId = null)
            }
            composable("${NavRoutes.BOOKING_FORM}/{routeId}") { backStackEntry ->
                val routeId = backStackEntry.arguments?.getString("routeId")
                BookingFormScreen(navController = navController, routeId = routeId)
            }
            composable(NavRoutes.BOOKING_SUCCESS) {
                BookingSuccessScreen(navController)
            }
            composable(NavRoutes.MY_BOOKINGS) {
                MyBookingsScreen()
            }
            composable(NavRoutes.SUPPORT) {
                SupportScreen()
            }
            composable(NavRoutes.SETTINGS) {
                SettingsScreen()
            }
            composable(NavRoutes.PROFILE) {
                // Временный заглушечный экран профиля — позже вынесем в отдельный файл.
                Text(text = "Profile screen")
            }
        }
    }
}
