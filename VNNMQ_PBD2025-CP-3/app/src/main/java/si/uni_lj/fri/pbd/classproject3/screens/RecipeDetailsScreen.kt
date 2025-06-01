// File: app/src/main/java/si/uni_lj/fri/pbd/classproject3/screens/RecipeDetailsScreen.kt
package si.uni_lj.fri.pbd.classproject3.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import si.uni_lj.fri.pbd.classproject3.data.RecipeRepository
import si.uni_lj.fri.pbd.classproject3.viewModels.DetailsViewModel
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.ui.graphics.Color
import si.uni_lj.fri.pbd.classproject3.ui.theme.Blue40
import si.uni_lj.fri.pbd.classproject3.ui.theme.BlueGrey40
import si.uni_lj.fri.pbd.classproject3.ui.theme.Purple40
import si.uni_lj.fri.pbd.classproject3.ui.theme.PurpleGrey40

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeDetailsScreen(
    recipeId: String?,
    localOnly: Boolean,
    navController: NavHostController? = null
) {
    if (recipeId == null) {
        Text("No ID provided")
        return
    }

    val context = LocalContext.current
    val vm: DetailsViewModel = viewModel(
        factory = DetailsViewModel.Factory(
            repo      = RecipeRepository(context),
            recipeId  = recipeId,
            localOnly = localOnly
        )
    )

    val ui by vm.state.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(ui.details?.strMeal ?: "Recipe details") },
                navigationIcon = {
                    navController?.let { nav ->
                        IconButton(onClick = { nav.navigateUp() }) {
                            Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                        }
                    }
                },
                actions = {
                    IconButton(onClick = { vm.toggleFavorite() }) {
                        if (ui.isFavorite) Icon(Icons.Filled.Star, contentDescription = "Unfavorite")
                        else               Icon(Icons.Outlined.StarBorder, contentDescription = "Favorite")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(Modifier
            .padding(paddingValues)
            .fillMaxSize()
        ) {
            when {
                ui.isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                ui.error != null -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState()),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = ui.error.orEmpty(),
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.padding(24.dp)
                        )
                    }
                }

                ui.details != null -> {
                    val d = ui.details!!
                    val scrollState = rememberScrollState()

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(scrollState)
                            .padding(16.dp)
                    ) {
                        AsyncImage(
                            model = d.strMealThumb,
                            contentDescription = d.strMeal,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                        )
                        Spacer(Modifier.height(8.dp))
                        Text(
                            text = "Ingredients:",
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                        listOfNotNull(
                            d.strIngredient1?.takeIf(String::isNotBlank)
                                ?.let { it to (d.strMeasure1.orEmpty()) },
                            d.strIngredient2?.takeIf(String::isNotBlank)
                                ?.let { it to (d.strMeasure2.orEmpty()) },
                            d.strIngredient3?.takeIf(String::isNotBlank)
                                ?.let { it to (d.strMeasure3.orEmpty()) },
                            d.strIngredient4?.takeIf(String::isNotBlank)
                                ?.let { it to (d.strMeasure4.orEmpty()) },
                            d.strIngredient5?.takeIf(String::isNotBlank)
                                ?.let { it to (d.strMeasure5.orEmpty()) },
                            d.strIngredient6?.takeIf(String::isNotBlank)
                                ?.let { it to (d.strMeasure6.orEmpty()) },
                            d.strIngredient7?.takeIf(String::isNotBlank)
                                ?.let { it to (d.strMeasure7.orEmpty()) },
                            d.strIngredient8?.takeIf(String::isNotBlank)
                                ?.let { it to (d.strMeasure8.orEmpty()) },
                            d.strIngredient9?.takeIf(String::isNotBlank)
                                ?.let { it to (d.strMeasure9.orEmpty()) },
                            d.strIngredient10?.takeIf(String::isNotBlank)
                                ?.let { it to (d.strMeasure10.orEmpty()) },
                            d.strIngredient11?.takeIf(String::isNotBlank)
                                ?.let { it to (d.strMeasure11.orEmpty()) },
                            d.strIngredient12?.takeIf(String::isNotBlank)
                                ?.let { it to (d.strMeasure12.orEmpty()) },
                            d.strIngredient13?.takeIf(String::isNotBlank)
                                ?.let { it to (d.strMeasure13.orEmpty()) },
                            d.strIngredient14?.takeIf(String::isNotBlank)
                                ?.let { it to (d.strMeasure14.orEmpty()) },
                            d.strIngredient15?.takeIf(String::isNotBlank)
                                ?.let { it to (d.strMeasure15.orEmpty()) },
                            d.strIngredient16?.takeIf(String::isNotBlank)
                                ?.let { it to (d.strMeasure16.orEmpty()) },
                            d.strIngredient17?.takeIf(String::isNotBlank)
                                ?.let { it to (d.strMeasure17.orEmpty()) },
                            d.strIngredient18?.takeIf(String::isNotBlank)
                                ?.let { it to (d.strMeasure18.orEmpty()) },
                            d.strIngredient19?.takeIf(String::isNotBlank)
                                ?.let { it to (d.strMeasure19.orEmpty()) },
                            d.strIngredient20?.takeIf(String::isNotBlank)
                                ?.let { it to (d.strMeasure20.orEmpty()) }
                        ).forEach { (ing, qty) ->
                            Text(
                                "- $ing: $qty",
                                modifier = Modifier.padding(start = 8.dp, bottom = 2.dp)
                            )
                        }
                        Spacer(Modifier.height(8.dp))
                        Text(
                            text = "Instructions:",
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                        Text(
                            text = d.strInstructions.orEmpty(),
                            modifier = Modifier.padding(4.dp)
                        )
                    }
                }
            }
        }
    }
}
