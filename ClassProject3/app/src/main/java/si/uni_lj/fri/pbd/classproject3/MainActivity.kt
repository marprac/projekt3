package si.uni_lj.fri.pbd.classproject3

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.FavoriteBorder
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
import androidx.navigation.NavDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import kotlinx.serialization.Serializable
import si.uni_lj.fri.pbd.classproject3.screens.FavouritesScreen
import si.uni_lj.fri.pbd.classproject3.screens.FavouritesViewModel
import si.uni_lj.fri.pbd.classproject3.screens.RecipeDetailsScreen
import si.uni_lj.fri.pbd.classproject3.screens.RecipeDetailsViewModel
import si.uni_lj.fri.pbd.classproject3.screens.SearchScreen
import si.uni_lj.fri.pbd.classproject3.screens.SearchViewModel

class MainActivity : ComponentActivity() {
    //ViewModels
    private var sViewModel: SearchViewModel? = null
    private var rViewModel: RecipeDetailsViewModel? = null
    private var fViewModel: FavouritesViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sViewModel = ViewModelProvider(this).get(SearchViewModel::class.java)
        rViewModel = ViewModelProvider(this).get(RecipeDetailsViewModel::class.java)
        fViewModel = ViewModelProvider(this).get(FavouritesViewModel::class.java)
        setContent {
            //Start in splash screen
            SplashScreen()
        }
    }

    //Navigation routes to screens
    @Serializable
    sealed class NavRoutes(val route: String) {
        @Serializable
        object SearchScreen : NavRoutes("search")
        @Serializable
        object FavouritesScreen : NavRoutes("favourites")
        @Serializable
        object RecipeDetailsScreen : NavRoutes("recipeDetails")

    }

    //Navigation bar
    data class TopLevelRoute(val name: String, val navRoute: String, val icon: ImageVector)


    @Composable
    fun SplashScreen() {
        //If first start of app show loading screen
        if(sViewModel?.startTime?.toInt() == 0)sViewModel?.startTime = System.currentTimeMillis()
        val isLoading = sViewModel?.isLoading?.observeAsState()
        if(isLoading?.value == true && sViewModel?.startTime?.plus(3000)!! > System.currentTimeMillis()){
            sViewModel?.getAllIngredients()
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
                Text(text = "App is loading", textAlign = TextAlign.Center, fontWeight = FontWeight.Bold, fontSize = 20.sp)
            }
        }else{
            //If ingredients loaded or timeout show main screen
            MainScreen()
        }

    }

    @Composable
    fun MainScreen() {
        val isOnline = true
        val recipeId = ""
        val topLevelRoutes = listOf(
            TopLevelRoute("Search", NavRoutes.SearchScreen.route, Icons.Default.Search),
            TopLevelRoute("Favourites", NavRoutes.FavouritesScreen.route, Icons.Default.FavoriteBorder),
            TopLevelRoute("Recipe Details", NavRoutes.RecipeDetailsScreen.route + "/$recipeId/$isOnline", Icons.Default.Clear)
        )
        val navController = rememberNavController()


        Scaffold(
            bottomBar = {
                NavigationBar {
                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentDestination: NavDestination? = navBackStackEntry?.destination

                    topLevelRoutes.forEach { item ->
                        NavigationBarItem(
                            selected = currentDestination?.route == item.navRoute,
                            onClick = {
                                navController.navigate(item.navRoute) {
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = { Icon(item.icon, contentDescription = item.name) },
                            label = { Text(item.name) }
                        )
                    }
                }
            }

        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = NavRoutes.SearchScreen.route,
                modifier = Modifier.padding(innerPadding)
            ) {
                //Create search screen
                composable(NavRoutes.SearchScreen.route) {
                    SearchScreen(viewModel = sViewModel!!, navigateToRecipeDetails = {
                        recipeId ->
                        val isOnline = true
                        navController.navigate("${NavRoutes.RecipeDetailsScreen.route}/$recipeId/$isOnline")
                    })
                }
                //Create favourites screen
                composable(NavRoutes.FavouritesScreen.route) {
                    FavouritesScreen(fViewModel!!, navigateToRecipeDetails = {
                        recipeId ->
                        val isOnline = false
                        navController.navigate("${NavRoutes.RecipeDetailsScreen.route}/$recipeId/$isOnline")
                    })
                }
                //Create recipe details screen
                composable(NavRoutes.RecipeDetailsScreen.route + "/{recipeId}" + "/{isOnline}") {
                    backStackEntry ->
                    //Get recipeId and isOnline from navigation
                    val id = backStackEntry.arguments?.getString("recipeId") ?: "-1"
                    val isOnline = backStackEntry.arguments?.getString("isOnline") ?: "true"
                    RecipeDetailsScreen(viewModel = rViewModel!!, id = id, isOnline = isOnline)
                }
            }
        }
    }

    //Preview
    @Preview
    @Composable
    fun MainScreenPreview() {
        MainScreen()
    }
}