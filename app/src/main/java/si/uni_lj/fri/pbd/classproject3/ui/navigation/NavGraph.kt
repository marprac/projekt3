package si.uni_lj.fri.pbd.classproject3.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import si.uni_lj.fri.pbd.classproject3.ui.screens.*

object Destinations {
    const val SPLASH = "splash"
    const val SEARCH = "search"
    const val FAVORITES = "favorites"
    const val DETAILS = "details/{id}"

    fun details(id: String) = "details/$id"
}

@Composable
fun AppNavGraph(modifier: Modifier = Modifier) {
    val nav = rememberNavController()

    NavHost(navController = nav, startDestination = Destinations.SPLASH, modifier = modifier) {

        composable(Destinations.SPLASH) {
            SplashScreen(onTimeout = { nav.navigate(Destinations.SEARCH) {
                popUpTo(Destinations.SPLASH) { inclusive = true }
            }})
        }

        composable(Destinations.SEARCH) {
            SearchScreen(onRecipeClick = { nav.navigate(Destinations.details(it)) },
                onOpenFav = { nav.navigate(Destinations.FAVORITES) })
        }

        composable(Destinations.FAVORITES) {
            FavoritesScreen(
                onBack = { nav.popBackStack() },
                onRecipeClick = { nav.navigate(Destinations.details(it)) }
            )
        }

        composable(
            Destinations.DETAILS,
            arguments = listOf(navArgument("id") { type = NavType.StringType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id") ?: return@composable
            RecipeDetailsScreen(id = id, onBack = { nav.popBackStack() })
        }
    }
}