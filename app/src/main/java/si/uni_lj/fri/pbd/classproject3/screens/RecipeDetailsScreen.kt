package si.uni_lj.fri.pbd.classproject3.screens
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember // To remember the processed ingredients list
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import si.uni_lj.fri.pbd.classproject3.models.RecipeDetailsIM
import si.uni_lj.fri.pbd.classproject3.viewmodels.DetailsViewModel
import si.uni_lj.fri.pbd.classproject3.viewmodels.DetailsViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeDetailsScreen(
    onNavigateBack: () -> Unit = {},
    recipeId: String,
    fromSearch: Boolean = true
) {
    val viewModel: DetailsViewModel = viewModel(
        factory = DetailsViewModelFactory(recipeId, fromSearch)
    )

    val recipeUiState by viewModel.recipeUiState.collectAsStateWithLifecycle()

    val isFavoriteDisplay = recipeUiState.isFavorite == true

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = recipeUiState.strMeal ?: "Recipe Details",
                        maxLines = 1
                    )
                },
                actions = {
                    if (!recipeUiState.isLoading && recipeUiState.errorMessage == null && recipeUiState.idMeal != null) {
                        IconButton(onClick = { viewModel.toggleFavorite() }) {
                            Icon(
                                imageVector = if (isFavoriteDisplay) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                                contentDescription = if (isFavoriteDisplay) "Remove from favorites" else "Add to favorites",
                                tint = if (isFavoriteDisplay) MaterialTheme.colorScheme.primary else LocalContentColor.current
                            )
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                recipeUiState.isLoading -> {
                    LoadingIndicator()
                }
                recipeUiState.errorMessage != null -> {
                    ErrorView(
                        message = recipeUiState.errorMessage ?: "An unknown error occurred.",
                        onRetry = { viewModel.refreshRecipeData() }
                    )
                }
                recipeUiState.idMeal != null -> {
                    RecipeContentView(recipeDetails = recipeUiState)
                }
                else -> {
                    ErrorView(message = "Recipe data is unavailable.", onRetry = { viewModel.refreshRecipeData() })
                }
            }
        }
    }
}

@Composable
fun LoadingIndicator() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun ErrorView(message: String, onRetry: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Filled.Info,
            contentDescription = "Error",
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.error
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.error
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onRetry) {
            Text("Retry")
        }
    }
}

@Composable
fun RecipeContentView(recipeDetails: RecipeDetailsIM) {
    val uriHandler = LocalUriHandler.current
    val ingredientsList = remember(recipeDetails) {
        buildList<Pair<String, String>> {
            val ingredients = listOfNotNull(
                recipeDetails.strIngredient1, recipeDetails.strIngredient2, recipeDetails.strIngredient3,
                recipeDetails.strIngredient4, recipeDetails.strIngredient5, recipeDetails.strIngredient6,
                recipeDetails.strIngredient7, recipeDetails.strIngredient8, recipeDetails.strIngredient9,
                recipeDetails.strIngredient10, recipeDetails.strIngredient11, recipeDetails.strIngredient12,
                recipeDetails.strIngredient13, recipeDetails.strIngredient14, recipeDetails.strIngredient15,
                recipeDetails.strIngredient16, recipeDetails.strIngredient17, recipeDetails.strIngredient18,
                recipeDetails.strIngredient19, recipeDetails.strIngredient20
            )
            val measures = listOfNotNull(
                recipeDetails.strMeasure1, recipeDetails.strMeasure2, recipeDetails.strMeasure3,
                recipeDetails.strMeasure4, recipeDetails.strMeasure5, recipeDetails.strMeasure6,
                recipeDetails.strMeasure7, recipeDetails.strMeasure8, recipeDetails.strMeasure9,
                recipeDetails.strMeasure10, recipeDetails.strMeasure11, recipeDetails.strMeasure12,
                recipeDetails.strMeasure13, recipeDetails.strMeasure14, recipeDetails.strMeasure15,
                recipeDetails.strMeasure16, recipeDetails.strMeasure17, recipeDetails.strMeasure18,
                recipeDetails.strMeasure19, recipeDetails.strMeasure20
            )

            for (i in ingredients.indices) {
                val ingredient = ingredients[i].trim()
                if (ingredient.isNotBlank()) {
                    val measure = if (i < measures.size) measures[i].trim() else ""
                    add(Pair(ingredient, measure))
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(recipeDetails.strMealThumb)
                .crossfade(true)
                .build(),
            contentDescription = recipeDetails.strMeal ?: "Recipe image",
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16f / 9f)
                .clip(MaterialTheme.shapes.medium),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            recipeDetails.strCategory?.let {
                Text(text = "Category: $it", style = MaterialTheme.typography.bodyMedium)
            }
            recipeDetails.strArea?.let {
                Text(text = "Area: $it", style = MaterialTheme.typography.bodyMedium)
            }
        }
        if (recipeDetails.strCategory != null || recipeDetails.strArea != null) {
            Spacer(modifier = Modifier.height(16.dp))
        }

        Text(
            text = "Ingredients:",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.height(8.dp))

        if (ingredientsList.isNotEmpty()) {
            ingredientsList.forEach { (ingredient, measure) ->
                val ingredientText = buildAnnotatedString {
                    append("• ")
                    append(ingredient)
                    if (measure.isNotBlank()) {
                        append(":")
                        withStyle(style = SpanStyle(fontStyle = FontStyle.Italic, color = MaterialTheme.colorScheme.onSurfaceVariant)) {
                            append(" $measure")
                        }
                    }
                }
                Text(
                    text = ingredientText,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(4.dp))
            }
        } else {
            Text("No ingredients listed.", style = MaterialTheme.typography.bodyLarge)
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Instructions:",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = recipeDetails.strInstructions?.trim() ?: "No instructions provided.",
            style = MaterialTheme.typography.bodyLarge,
            lineHeight = 24.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        recipeDetails.strYoutube?.let { youtubeUrl ->
            if (youtubeUrl.isNotBlank()) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Watch on YouTube: $youtubeUrl",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.primary,
                        textDecoration = TextDecoration.Underline
                    ),
                    modifier = Modifier.clickable {
                        try {
                            uriHandler.openUri(youtubeUrl)
                        } catch (e: Exception) {
                            Log.e("RecipeContentView", "Could not open YouTube link: $youtubeUrl", e)
                        }
                    }
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}