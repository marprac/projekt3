package si.uni_lj.fri.pbd.classproject3.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.pullToRefresh
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import si.uni_lj.fri.pbd.classproject3.viewmodels.SearchViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import si.uni_lj.fri.pbd.classproject3.models.dto.IngredientDTO
import si.uni_lj.fri.pbd.classproject3.models.dto.RecipeSummaryDTO

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    viewModel: SearchViewModel = viewModel(),
    onRecipeClick: (String) -> Unit
) {
    val recipes by viewModel.recipes.observeAsState(initial = emptyList())
    val ingredients by viewModel.ingredients.observeAsState(initial = emptyList())
    val error by viewModel.error.observeAsState()
    val isLoading by viewModel.isLoading.observeAsState(initial = false)


    var selectedIngredient by remember { mutableStateOf<IngredientDTO?>(null) }
    var expanded by remember { mutableStateOf(false) }

    val pullRefreshState = rememberPullToRefreshState()
    var isRefreshing by remember { mutableStateOf(false) }
    val onRefresh: () -> Unit = {
        isRefreshing = true
        selectedIngredient?.strIngredient?.let { ingredientName ->
            viewModel.searchRecipesByMainIngredient(ingredientName)
        }
        isRefreshing = false
    }


    Scaffold { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .pullToRefresh(
                        state = pullRefreshState,
                        isRefreshing = isLoading,
                        onRefresh = onRefresh

                    )
            ) {
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    OutlinedTextField(
                        value = selectedIngredient?.strIngredient ?: "Select an Ingredient",
                        onValueChange = { },
                        readOnly = true,
                        label = { Text("Ingredient") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(
                                type = MenuAnchorType.PrimaryNotEditable,
                                enabled = true
                            )
                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        if (ingredients.isEmpty() && isLoading) {
                            DropdownMenuItem(
                                text = { Text("Loading ingredients...") },
                                onClick = { },
                                enabled = false
                            )
                        } else if (ingredients.isEmpty()) {
                            DropdownMenuItem(
                                text = { Text("No ingredients available") },
                                onClick = { },
                                enabled = false
                            )
                        }
                        ingredients.forEach { ingredient ->
                            DropdownMenuItem(
                                text = { Text(ingredient.strIngredient ?: "Unknown Ingredient") },
                                onClick = {
                                    selectedIngredient = ingredient
                                    expanded = false
                                    ingredient.strIngredient?.let {
                                        viewModel.searchRecipesByMainIngredient(it)
                                    }
                                }
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))

                error?.let {
                    Text(
                        text = it,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                if (isLoading && !isRefreshing) {
                    Box(modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }

                if (!isLoading && recipes.isEmpty() && error == null && selectedIngredient != null) {
                    Text(
                        text = "No recipes found for '${selectedIngredient?.strIngredient}'.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                } else if (!isLoading && selectedIngredient == null && recipes.isEmpty() && error == null) {
                    Text(
                        text = "Please select an ingredient to search for recipes.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                } else if (recipes.isNotEmpty()) { // Show recipes only if not empty
                    Text("Recipes:", style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(8.dp))
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        modifier = Modifier.weight(1f),
                        contentPadding = PaddingValues(vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(recipes) { recipe ->
                            RecipeGridItem(recipe = recipe, onRecipeClick = onRecipeClick)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun RecipeGridItem(recipe: RecipeSummaryDTO, onRecipeClick: (String) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .clickable { recipe.idMeal?.let { onRecipeClick(it) } },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            Image(
                painter = rememberAsyncImagePainter(
                    ImageRequest.Builder(LocalContext.current)
                        .data(data = recipe.strMealThumb)
                        .crossfade(true)
                        .build()
                ),
                contentDescription = recipe.strMeal,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentScale = ContentScale.Crop
            )
            Text(
                text = recipe.strMeal ?: "Unknown Recipe",
                style = MaterialTheme.typography.titleSmall,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}