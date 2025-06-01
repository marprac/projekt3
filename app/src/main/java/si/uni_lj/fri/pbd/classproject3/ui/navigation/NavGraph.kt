package si.uni_lj.fri.pbd.classproject3.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import si.uni_lj.fri.pbd.classproject3.ui.screens.MainScreen
import si.uni_lj.fri.pbd.classproject3.ui.screens.RecipeDetailsScreen
import si.uni_lj.fri.pbd.classproject3.ui.screens.SplashScreen

object Destinations {
    const val SPLASH = "splash"
    const val MAIN = "main"
    const val SEARCH = "search"
    const val FAVORITES = "favorites"
    const val DETAILS = "details/{id}/{isOnline}"

    fun details(id: String, isOnline: Boolean) = "details/$id/$isOnline"
}

@Composable
fun AppNavGraph(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Destinations.SPLASH, modifier = modifier) {

        composable(Destinations.SPLASH) {
            SplashScreen(onTimeout = {
                navController.navigate(Destinations.MAIN) {
                    popUpTo(Destinations.SPLASH) { inclusive = true }
                }
            })
        }

        composable(Destinations.MAIN) {
            MainScreen(mainNavController = navController)
        }

        composable(
            Destinations.DETAILS,
            arguments = listOf(
                navArgument("id") { type = NavType.StringType },
                navArgument("isOnline") { type = NavType.BoolType }
            )
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id") ?: return@composable
            RecipeDetailsScreen(onBack = { navController.popBackStack() })
        }
    }
}