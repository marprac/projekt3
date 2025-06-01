package si.uni_lj.fri.pbd.classproject3.screens

import android.annotation.SuppressLint
import android.app.Application
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import si.uni_lj.fri.pbd.classproject3.viewmodels.SearchViewModel

sealed class Screen(val route: String, val title: String, val icon: ImageVector? = null) {
    data object Search : Screen("search", "Search", Icons.Default.Search)
    data object Favorites: Screen("favorites", "Favorites", Icons.Default.Favorite)
    data object RecipeDetails: Screen("recipeDetails/{recipeId}/{fromSearch}", "Details") {
        fun createRoute(recipeId: String, fromSearch: Boolean) = "recipeDetails/$recipeId/$fromSearch"
    }
}

val items = listOf(
    Screen.Search,
    Screen.Favorites
)

@Composable
fun MainScreen(searchViewModel: SearchViewModel) {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController)
        }
    ) {
        innerPadding ->
        AppNavHost(
            navController = navController,
            searchViewModel = searchViewModel,
            innerPadding = innerPadding
        )
    }
}

@Composable
fun AppNavHost(
    navController: NavHostController,
    searchViewModel: SearchViewModel,
    innerPadding: PaddingValues) {
    NavHost(
        navController = navController,
        startDestination = Screen.Search.route,
        modifier = Modifier.padding(innerPadding)
    ) {
        composable(Screen.Search.route) {
            SearchScreen(
                viewModel = searchViewModel,
                onRecipeClick = { recipeId ->
                navController.navigate(Screen.RecipeDetails.createRoute(recipeId, true))
            })
        }
        composable(Screen.Favorites.route) {
            FavoritesScreen(onRecipeClick = { recipeId ->
                navController.navigate(Screen.RecipeDetails.createRoute(recipeId, false))
            })
        }
        composable(
            route = Screen.RecipeDetails.route,
            arguments = listOf(
                navArgument("recipeId") { type = NavType.StringType },
                navArgument("fromSearch") { type = NavType.BoolType }
            )

        ) { backStackEntry ->
            val recipeId = backStackEntry.arguments?.getString("recipeId")
            val fromSearch = backStackEntry.arguments?.getBoolean("fromSearch") == true
            if (recipeId != null) {
                RecipeDetailsScreen(
                    onNavigateBack = { navController.popBackStack() },
                    recipeId = recipeId,
                    fromSearch = fromSearch
                )
            } else {
                // TODO: Handle case where recipeId is null
            }
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    NavigationBar {
        val navBackStackEntry: NavBackStackEntry? = navController.currentBackStackEntryAsState().value
        val currentRoute = navBackStackEntry?.destination?.route

        items.forEach { screen ->
            NavigationBarItem(
                icon = {
                    screen.icon?.let {
                        Icon(it, contentDescription = screen.title)
                    }
                },
                label = { Text(screen.title) },
                selected = currentRoute == screen.route ||
                        (screen == Screen.RecipeDetails && currentRoute?.startsWith(Screen.RecipeDetails.route.substringBefore("/")) == true),
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        restoreState = true
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}
