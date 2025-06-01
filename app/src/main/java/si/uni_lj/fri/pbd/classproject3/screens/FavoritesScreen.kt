package si.uni_lj.fri.pbd.classproject3.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import si.uni_lj.fri.pbd.classproject3.R // For placeholder/error drawables
import si.uni_lj.fri.pbd.classproject3.models.RecipeSummaryIM
import si.uni_lj.fri.pbd.classproject3.viewmodels.FavoritesViewModel

@Composable
fun FavoritesScreen(
    viewModel: FavoritesViewModel = viewModel(),
    onRecipeClick: (recipeId: String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Surface(modifier = Modifier.fillMaxSize()) {
        when {
            uiState.isLoading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            uiState.errorMessage != null -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Error: ${uiState.errorMessage}")
                }
            }
            uiState.favoriteRecipes.isEmpty() -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No favorite recipes found.")
                }
            }
            else -> {
                FavoriteRecipesGallery(
                    recipes = uiState.favoriteRecipes,
                    onRecipeClick = onRecipeClick
                )
            }
        }
    }
}

@Composable
fun FavoriteRecipesGallery(
    recipes: List<RecipeSummaryIM>,
    onRecipeClick: (recipeId: String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 150.dp), // Adjust minSize as needed
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier.fillMaxSize()
    ) {
        items(recipes, key = { it.idMeal }) { recipe -> // Use a stable key
            FavoriteRecipeCard(
                recipe = recipe,
                onClick = { onRecipeClick(recipe.idMeal) }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoriteRecipeCard(
    recipe: RecipeSummaryIM,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1f),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(recipe.strMealThumb)
                    .crossfade(true)
                    .build(),
                placeholder = painterResource(R.drawable.ic_placeholder_image),
                error = painterResource(R.drawable.ic_error_image),
                contentDescription = recipe.strMeal,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )
            Text(
                text = recipe.strMeal,
                style = MaterialTheme.typography.titleSmall,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                maxLines = 2
            )
        }
    }
}