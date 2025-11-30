
package np.mad.assignment.mad_assignment_t01_team1

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
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
}


@Composable
fun MainNavigation(
    // You can pass dependencies or viewModels if needed
) {
    val navController = rememberNavController()
    val tabs = listOf(
        AppScreen.Home,
        AppScreen.Canteen,
        AppScreen.Favorite,
        AppScreen.Profile
    )

    Scaffold(
        bottomBar = {
            NavigationBar {
                val currentBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = currentBackStackEntry?.destination

                tabs.forEach { screen ->
                    val selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true
                    NavigationBarItem(
                        selected = selected,
                        onClick = {
                            navController.navigate(screen.route) {
                                // Keep only one instance of the destination & restore previous state
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
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = AppScreen.Favorite.route, // Start at Favorites per your preference
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(AppScreen.Home.route) {
                //HomeScreenPlaceholder()
            }
            composable(AppScreen.Canteen.route) {
                //CanteenScreenPlaceholder()
            }
            composable(AppScreen.Favorite.route) {
                // Hook your existing FavoriteScreen here
                FavoriteScreen(
                    onStallClick = { stall ->
                        navController.navigate("stall/${stall.stallId}")
                    },
                    onUnfavorite = { /* persist removal via ViewModel/Repo */ }
                )
            }
            composable(AppScreen.Profile.route) {
                //ProfileScreenPlaceholder()
            }
            // Optional: stall detail route with argument
            /*
            composable(
                route = AppScreen.StallDetail.route,
                arguments = listOf(navArgument("stallId") { type = NavType.LongType })
            ) { backStackEntry ->
                val stallId = backStackEntry.arguments?.getLong("stallId") ?: -1L
                StallDetailPlaceholder(stallId)
            }*/
        }
    }
}
