package si.uni_lj.fri.pbd.classproject3.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshIndicator
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import si.uni_lj.fri.pbd.classproject3.models.RecipeSummaryIM
import si.uni_lj.fri.pbd.classproject3.repository.RecipeRepository
import si.uni_lj.fri.pbd.classproject3.ui.components.RecipeCard
import si.uni_lj.fri.pbd.classproject3.ui.viewmodel.SearchViewModel

@Composable
fun SearchScreen(
    repo: RecipeRepository = remember { /* obtain singletons however you prefer */ },
    onRecipeClick: (String) -> Unit,
    onOpenFav: () -> Unit
) {
    val vm = remember { SearchViewModel(repo) }

    val ingredients by vm.ingredients.collectAsState()
    val selected by vm.selectedIngredient.collectAsState()
    val recipes by vm.recipes.collectAsState()
    val isRefreshing by vm.isRefreshing.collectAsState()

    var tab by remember { mutableStateOf(0) } // 0 = search, 1 = favorites

    Scaffold(topBar = {
        TopAppBar(
            title = { Text("Recipes") },
            actions = {
                IconButton(onClick = onOpenFav) {
                    Icon(Icons.Default.Favorite, contentDescription = "Favorites")
                }
            })
    }) { padding ->

        Column(Modifier.padding(padding)) {
            /** ----- Ingredient dropdown ----- **/
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
                                vm.selectIngredient(ing)
                            }
                        )
                    }
                }
            }

            /** ----- Tabs ----- **/
            val titles = listOf("All", "Favorites")
            TabRow(selectedTabIndex = tab) {
                titles.forEachIndexed { index, title ->
                    Tab(
                        selected = tab == index,
                        onClick = { tab = index },
                        text = { Text(title) },
                        icon = {
                            if (index == 0) Icon(Icons.Default.Search, null)
                            else Icon(Icons.Default.Favorite, null)
                        }
                    )
                }
            }

            /** ----- Content: grid with swipe refresh ----- **/
            val listToShow: List<RecipeSummaryIM> =
                if (tab == 0) recipes else emptyList() // Favorites in separate screen

            SwipeRefresh(
                state = rememberSwipeRefreshState(isRefreshing),
                onRefresh = { vm.refresh() },
                indicator = { s, d ->
                    SwipeRefreshIndicator(
                        state = s, refreshTriggerDistance = d,
                        scale = true
                    )
                },
                modifier = Modifier.fillMaxSize()
            ) {
                if (listToShow.isEmpty()) {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(
                            text = if (selected == null) "Choose an ingredient"
                            else "No recipes for $selected"
                        )
                    }
                } else {
                    LazyVerticalGrid(
                        columns = GridCells.Adaptive(minSize = 140.dp),
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(4.dp)
                    ) {
                        items(listToShow.size) { idx ->
                            RecipeCard(recipe = listToShow[idx], onClick = onRecipeClick)
                        }
                    }
                }
            }
        }
    }
}
