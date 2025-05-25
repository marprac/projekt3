package si.uni_lj.fri.pbd.classproject3.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import si.uni_lj.fri.pbd.classproject3.ui.components.RecipeCard
import si.uni_lj.fri.pbd.classproject3.ui.viewmodel.FavoritesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(
    onRecipeClick: (String) -> Unit,
    viewModel: FavoritesViewModel = hiltViewModel()
) {
    val favorites by viewModel.favorites.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()


    Scaffold(topBar = {
        TopAppBar(
            title = { Text("Favorites") }
        )
    }) { padding ->
        if (errorMessage != null) {
            Box(
                Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text(errorMessage!!)
            }
        } else if (favorites.isEmpty()) {
            Box(
                Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text("No favorite recipes yet.")
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 140.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(4.dp)
            ) {
                items(
                    count = favorites.size,
                    key = { index -> favorites[index].idMeal }
                ) { index ->
                    val recipe = favorites[index]
                    RecipeCard(recipe = recipe, onClick = onRecipeClick)
                }
            }
        }
    }
}
