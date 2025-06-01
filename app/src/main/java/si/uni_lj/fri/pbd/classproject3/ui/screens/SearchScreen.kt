package si.uni_lj.fri.pbd.classproject3.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshIndicator
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import si.uni_lj.fri.pbd.classproject3.models.RecipeSummaryIM
import si.uni_lj.fri.pbd.classproject3.ui.components.RecipeCard
import si.uni_lj.fri.pbd.classproject3.ui.viewmodel.SearchViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    onRecipeClick: (String) -> Unit,
    viewModel: SearchViewModel = hiltViewModel()
) {
    val ingredients by viewModel.ingredients.collectAsState()
    val selected by viewModel.selectedIngredient.collectAsState()
    val recipes by viewModel.recipes.collectAsState()
    val isRefreshing by viewModel.isRefreshing.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()


    Scaffold(topBar = {
        TopAppBar(
            title = { Text("Recipes") }
        )
    }) { padding ->

        Column(Modifier.padding(padding)) {
            var expanded by remember { mutableStateOf(false) }

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                TextField(
                    value = selected ?: "Select ingredient",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Ingredient") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )

                ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    ingredients.forEach { ing ->
                        DropdownMenuItem(
                            text = { Text(ing) },
                            onClick = {
                                expanded = false
                                viewModel.selectIngredient(ing)
                            }
                        )
                    }
                }
            }


            SwipeRefresh(
                state = rememberSwipeRefreshState(isRefreshing),
                onRefresh = { viewModel.refresh() },
                indicator = { s, d ->
                    SwipeRefreshIndicator(
                        state = s, refreshTriggerDistance = d,
                        scale = true
                    )
                },
                modifier = Modifier.fillMaxSize()
            ) {
                if (errorMessage != null) {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(text = errorMessage!!)
                    }
                } else if (recipes.isEmpty() && selected != null && !isRefreshing) {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(
                            text = "No recipes for $selected"
                        )
                    }
                } else if (selected == null && !isRefreshing) {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(text = "Choose an ingredient")
                    }
                } else if (recipes.isNotEmpty()){
                    LazyVerticalGrid(
                        columns = GridCells.Adaptive(minSize = 140.dp),
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(4.dp)
                    ) {
                        items(recipes.size) { idx ->
                            RecipeCard(recipe = recipes[idx], onClick = onRecipeClick)
                        }
                    }
                } else if (isRefreshing) {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
            }
        }
    }
}