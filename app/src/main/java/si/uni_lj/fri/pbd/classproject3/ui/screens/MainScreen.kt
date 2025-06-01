package si.uni_lj.fri.pbd.classproject3.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import si.uni_lj.fri.pbd.classproject3.ui.navigation.Destinations

sealed class BottomNavItem(val route: String, val icon: ImageVector, val label: String) {
    object Search : BottomNavItem(Destinations.SEARCH, Icons.Filled.Search, "Search")
    object Favorites : BottomNavItem(Destinations.FAVORITES, Icons.Filled.Favorite, "Favorites")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(mainNavController: NavHostController) {
    val bottomBarNavController = rememberNavController()
    val navItems = listOf(BottomNavItem.Search, BottomNavItem.Favorites)

    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by bottomBarNavController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

                navItems.forEach { screen ->
                    NavigationBarItem(
                        icon = { Icon(screen.icon, contentDescription = screen.label) },
                        label = { Text(screen.label) },
                        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                        onClick = {
                            bottomBarNavController.navigate(screen.route) {
                                popUpTo(bottomBarNavController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = bottomBarNavController,
            startDestination = Destinations.SEARCH,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Destinations.SEARCH) {
                SearchScreen(
                    onRecipeClick = { recipeId ->
                        mainNavController.navigate(Destinations.details(recipeId, true))
                    }
                )
            }
            composable(Destinations.FAVORITES) {
                FavoritesScreen(
                    onRecipeClick = { recipeId ->
                        mainNavController.navigate(Destinations.details(recipeId, false))
                    }
                )
            }
        }
    }
}