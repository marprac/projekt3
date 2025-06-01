
package si.uni_lj.fri.pbd.classproject3.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import si.uni_lj.fri.pbd.classproject3.utils.RecipeCard
import si.uni_lj.fri.pbd.classproject3.viewModels.FavoritesViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun FavoritesScreen(
    navController: NavHostController,
    vm: FavoritesViewModel = viewModel()
) {
    val favs by vm.favorites.collectAsState()

    if (favs.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "No favorites yet",
                style = MaterialTheme.typography.bodyLarge
            )
        }
    } else {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(140.dp),
            contentPadding = PaddingValues(8.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(favs) { recipe ->
                RecipeCard(
                    recipe = recipe,
                    onClick = {
                        navController.navigate("details/${recipe.idMeal}?localOnly=true")
                    }
                )
            }
        }
    }
}

