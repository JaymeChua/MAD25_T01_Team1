package np.mad.assignment.mad_assignment_t01_team1

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
            startDestination = AppScreen.Favorite.route,
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
                        userId = 2L,
                        canteen = it, // Pass the selected canteen to filter food stalls
                        navController = navController,
                    )
                }
            }
            composable("review/{stallId}") { backStackEntry ->
                val stallId = backStackEntry.arguments?.getString("stallId")?.toLong() ?: 1L

                ReviewPage(
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
                    userId = 2L,
                    onStallClick = { stall ->
                        navController.navigate("stall/${stall.stallId}")
                    },
                    onLoginClick = {
                        navController.navigate((AppScreen.Login.route))
                    }
                )
            }
            composable(AppScreen.Profile.route) {

            }
        }
    }
}
