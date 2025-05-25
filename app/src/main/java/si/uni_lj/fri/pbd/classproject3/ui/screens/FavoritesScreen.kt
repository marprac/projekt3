package si.uni_lj.fri.pbd.classproject3.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import si.uni_lj.fri.pbd.classproject3.repository.RecipeRepository
import si.uni_lj.fri.pbd.classproject3.ui.components.RecipeCard
import si.uni_lj.fri.pbd.classproject3.ui.viewmodel.FavoritesViewModel

@Composable
fun FavoritesScreen(
    repo: RecipeRepository = remember { /* whichever DI */ },
    onBack: () -> Unit,
    onRecipeClick: (String) -> Unit
) {
    val vm = remember { FavoritesViewModel(repo) }
    val favorites = vm.favorites.collectAsState()

    Scaffold(topBar = {
        TopAppBar(
            title = { Text("Favorites") },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
            }
        )
    }) { padding ->
        if (favorites.value.isEmpty()) {
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
                items(favorites.value.size) { idx ->
                    RecipeCard(recipe = favorites.value[idx], onClick = onRecipeClick)
                }
            }
        }
    }
}
