package np.mad.assignment.mad_assignment_t01_team1

import android.content.Context
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
    import androidx.navigation.compose.rememberNavController

sealed class AppScreen(val route: String, val label: String, val icon: ImageVector){
    data object Home    : AppScreen("home", "Home", Icons.Filled.Home)
    data object Canteen : AppScreen("canteen","Canteen", Icons.Filled.ShoppingCart)
    data object Favorite : AppScreen("favorite","Favorite", Icons.Filled.Star)
    data object Profile : AppScreen("profile", "Profile", Icons.Filled.Person)
    //data object StallDetail : AppScreen("stall/{stallId)", "Stall Detail", Icons.Filled.Star)
    data object Login : AppScreen("login", "Login", Icons.Filled.Lock)
}


@Composable
fun MainNavigation(
    userId: Long,
    userRole: String,
    onLogout: () -> Unit
) {
    val navController = rememberNavController()
    val context = LocalContext.current
    val tabs = listOf(
        AppScreen.Home,
        AppScreen.Canteen,
        AppScreen.Favorite,
        AppScreen.Profile
    )
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    Scaffold(
        bottomBar = {
            if (currentRoute != AppScreen.Login.route &&
                currentRoute != "register" &&
                userRole != "ADMIN") {
                NavigationBar {
                    val currentDestination = navBackStackEntry?.destination
                    tabs.forEach { screen ->
                        val selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true
                        NavigationBarItem(
                            selected = selected,
                            onClick = {
                                navController.navigate(screen.route) {
                                    popUpTo(navController.graph.startDestinationId) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = { Icon(imageVector = screen.icon, contentDescription = screen.label) },
                            label = { Text(screen.label) }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = if (userRole == "ADMIN") "Admin" else AppScreen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(AppScreen.Home.route) {
                HomeScreen(
                    onNavigateToCanteens = { canteenName ->
                        navController.navigate("stallDirectory/$canteenName")
                    },
                    onNavigateToStall = { stallId ->
                        navController.navigate("menu/$stallId")
                    }
                )
            }
            composable(AppScreen.Canteen.route) {
                CanteenDirectoryScreen(
                    canteens = getSampleCanteens(),
                    navController = navController,
                )
            }
            composable("stallDirectory/{canteenName}") { backStackEntry ->
                // Get the canteen name from the backstack arguments
                val canteenName = backStackEntry.arguments?.getString("canteenName")

                // Find the canteen based on the name
                val selectedCanteen = getSampleCanteens().firstOrNull { it.name == canteenName }

                // Pass the selected canteen data to StallDirectoryScreen
                selectedCanteen?.let {
                    StallDirectoryScreen(
                        userId = userId,
                        canteen = it, // Pass the selected canteen to filter food stalls
                        navController = navController,
                    )
                }
            }

            composable("review/{stallId}") { backStackEntry ->
                val stallId = backStackEntry.arguments?.getString("stallId")?.toLong() ?: 1L

                ReviewPage(
                    userId = userId,
                    stallId = stallId,
                    onCloseClicked = { navController.popBackStack() }
                )
            }
            composable("menu/{stallId}"){ backStackEntry ->
                val stallId = backStackEntry.arguments?.getString("stallId")?.toLong() ?: 4L
                MenuScreen(
                    stallId = stallId,
                    onBackClick = { navController.popBackStack() },
                    onReviewClick = {
                        navController.navigate("review/$stallId")
                    }
                )
            }
            composable(AppScreen.Favorite.route) {
                FavoriteScreen(
                    userId = userId,
                    onStallClick = { stall ->
                        navController.navigate("menu/${stall.stallId}")
                    },
                    onLoginClick = {
                        navController.navigate((AppScreen.Login.route))
                    }
                )
            }
            composable(AppScreen.Profile.route) {
                ProfileScreen(
                    userId = userId,
                    onLogout = {
                        onLogout()
                    }
                )
            }
            composable(AppScreen.Login.route) {
                val context = LocalContext.current
                LoginScreen(
                    onLoginSuccess = { newUserId: Long, userRole: String ->
                        val prefs = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
                        prefs.edit().putLong("logged_in_user", newUserId).apply()
                        prefs.edit().putString("user_role", userRole).apply()

                        navController.navigate(AppScreen.Home.route) {
                            popUpTo(AppScreen.Login.route) { inclusive = true }
                        }
                    },
                    onRegisterClick = {
                        navController.navigate("register")
                    }
                )
            }
            composable("register") {
                RegisterScreen(
                    onRegisterSuccess = {
                        navController.navigate(AppScreen.Login.route) {
                            popUpTo("register") { inclusive = true }
                        }
                    },
                    onCancel = { navController.popBackStack() }
                )
            }
            composable("Admin") {
                AdminDashboardScreen(
                    onManageStalls = { },
                    onManageDishes = { },
                    onManageUsers = {
                        println("DEBUG: Navigation triggered")
                        navController.navigate("userManagement")
                    },
                    onLogout = {
                        onLogout()
                    }
                )
            }
            composable("userManagement") {
                println("DEBUG: Arrived at UserManagementScreen")
                UserManagementScreen(
                    navController = navController,
                )
            }

        }
    }
}
