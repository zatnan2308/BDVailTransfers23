package com.example.bdvailtransfers2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
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

    val bottomItems = listOf(
        BottomNavItem(
            route = NavRoutes.HOME,
            icon = Icons.Default.Home,
            label = "Home"
        ),
        BottomNavItem(
            route = NavRoutes.BOOKING_FORM,
            icon = Icons.Default.Add,
            label = "Book"
        ),
        BottomNavItem(
            route = NavRoutes.MY_BOOKINGS,
            icon = Icons.Default.List,
            label = "My trips"
        ),
        BottomNavItem(
            route = NavRoutes.SETTINGS,
            icon = Icons.Default.Settings,
            label = "Settings"
        )
    )

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    val route = currentDestination?.route ?: NavRoutes.HOME
                    val title = when {
                        route.startsWith(NavRoutes.BOOKING_FORM) -> "Book transfer"
                        route == NavRoutes.ROUTES -> "Routes"
                        route == NavRoutes.MY_BOOKINGS -> "My trips"
                        route == NavRoutes.SUPPORT -> "Support"
                        route == NavRoutes.SETTINGS -> "Settings"
                        else -> "BDVail Transfers"
                    }
                    Text(text = title)
                }
            )
        },
        bottomBar = {
            NavigationBar {
                bottomItems.forEach { item ->
                    val selected =
                        currentDestination?.hierarchy?.any { it.route == item.route } == true ||
                                (item.route == NavRoutes.BOOKING_FORM &&
                                        (currentDestination?.route?.startsWith(NavRoutes.BOOKING_FORM) == true))

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
        }
    }
}
