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
                //HomeScreenPlaceholder()
            }
            composable(AppScreen.Canteen.route) {
                //Canteen directory page here:
                CanteenDirectoryScreen(
                    canteens = getSampleCanteens(), // Pass the sample canteen data here
                    navController = navController,
                    modifier = Modifier.padding(innerPadding)
                )
                //Stall directory page here:
                /*StallDirectoryScreen(
                    foodStalls = getSampleFoodStalls(), // Pass the sample data here
                    modifier = Modifier.padding(innerPadding)
                )*/
                //COMMENTED OUT REVIEW PAGE AS THIS IS MEANT FOR STALL DIREDTORY
                /*ReviewPage(
                    onCloseClicked = {
                        navController.navigate(AppScreen.Canteen.route) {
                        }
                    }
                )*/
            }
            composable("stallDirectory/{canteenName}") { backStackEntry ->
                // Get the canteen name from the backstack arguments
                val canteenName = backStackEntry.arguments?.getString("canteenName")

                // Find the canteen based on the name
                val selectedCanteen = getSampleCanteens().firstOrNull { it.name == canteenName }

                // Pass the selected canteen data to StallDirectoryScreen
                selectedCanteen?.let {
                    StallDirectoryScreen(
                        canteen = it, // Pass the selected canteen to filter food stalls
                        modifier = Modifier.padding(innerPadding)
                    )
                }
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
