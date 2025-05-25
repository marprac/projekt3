package si.uni_lj.fri.pbd.classproject3.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import si.uni_lj.fri.pbd.classproject3.ui.viewmodel.DetailsViewModel
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.AnnotatedString

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeDetailsScreen(
    onBack: () -> Unit,
    viewModel: DetailsViewModel = hiltViewModel()
) {
    val details by viewModel.details.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val ingredientsList by viewModel.ingredientsList.collectAsState()
    val uriHandler = LocalUriHandler.current

    Scaffold(topBar = {
        TopAppBar(
            title = { Text(details?.strMeal ?: "Recipe Details") },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(Icons.Default.ArrowBack, null)
                }
            },
            actions = {
                details?.let {
                    IconButton(onClick = { viewModel.toggleFavorite() }) {
                        if (it.isFavorite == true)
                            Icon(Icons.Default.Favorite, contentDescription = "Unfavorite")
                        else
                            Icon(Icons.Outlined.FavoriteBorder, contentDescription = "Favorite")
                    }
                }
            }
        )
    }) { padding ->
        Box(
            Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            if (isLoading) {
                CircularProgressIndicator()
            } else if (errorMessage != null) {
                Text(
                    text = errorMessage!!,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(16.dp)
                )
            } else {
                details?.let { d ->
                    Column(
                        Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                    ) {
                        AsyncImage(
                            model = d.strMealThumb,
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(1f)
                        )

                        Text(
                            "Ingredients:",
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(start = 8.dp, end = 8.dp, top = 8.dp)
                        )
                        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                            if (ingredientsList.isEmpty() && d.strIngredient1.isNullOrBlank()) {
                                Text("No ingredients listed.", modifier = Modifier.padding(bottom = 2.dp))
                            } else {
                                ingredientsList.forEach { (name, measure) ->
                                    Text("• $name – $measure", modifier = Modifier.padding(bottom = 2.dp))
                                }
                            }
                        }

                        Spacer(Modifier.height(8.dp))
                        Text(
                            "Instructions:",
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(start = 8.dp, end = 8.dp, top = 8.dp)
                        )
                        Text(d.strInstructions ?: "No instructions provided.", modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp))

                        d.strYoutube?.takeIf { it.isNotBlank() }?.let { youtubeUrl ->
                            Spacer(Modifier.height(8.dp))
                            Text(
                                "Youtube:",
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.padding(start = 8.dp, end = 8.dp, top = 8.dp)
                            )
                            ClickableText(
                                text = AnnotatedString(youtubeUrl),
                                onClick = { uriHandler.openUri(youtubeUrl) },
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
                                style = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.primary)
                            )
                        }
                        d.strSource?.takeIf { it.isNotBlank() }?.let { sourceUrl ->
                            Spacer(Modifier.height(8.dp))
                            Text(
                                "Source:",
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.padding(start = 8.dp, end = 8.dp, top = 8.dp)
                            )
                            ClickableText(
                                text = AnnotatedString(sourceUrl),
                                onClick = { uriHandler.openUri(sourceUrl) },
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
                                style = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.primary)
                            )

                        }
                    }
                } ?: if (!isLoading) {
                    Text("Recipe details not available.")
                } else {}
            }
        }
    }
}
