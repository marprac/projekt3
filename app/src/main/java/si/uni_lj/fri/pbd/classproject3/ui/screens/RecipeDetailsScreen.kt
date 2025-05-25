package si.uni_lj.fri.pbd.classproject3.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import si.uni_lj.fri.pbd.classproject3.repository.RecipeRepository
import si.uni_lj.fri.pbd.classproject3.ui.viewmodel.DetailsViewModel

@Composable
fun RecipeDetailsScreen(
    id: String,
    repo: RecipeRepository = remember { /* your DI */ },
    onBack: () -> Unit
) {
    val vm = remember { DetailsViewModel(repo) }
    LaunchedEffect(id) { vm.load(id) }

    val details by vm.details.collectAsState()

    Scaffold(topBar = {
        TopAppBar(
            title = { Text(details?.strMeal ?: "") },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(Icons.Default.ArrowBack, null)
                }
            },
            actions = {
                IconButton(onClick = { vm.toggleFavorite() }) {
                    if (details?.isFavorite == true)
                        Icon(Icons.Default.Favorite, contentDescription = "Unfavorite")
                    else
                        Icon(Icons.Outlined.FavoriteBorder, contentDescription = "Favorite")
                }
            }
        )
    }) { padding ->
        details?.let { d ->
            Column(
                Modifier
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
            ) {
                AsyncImage(
                    model = d.strMealThumb,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                )

                Text("Ingredients:", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(8.dp))
                d.ingredients.forEach { (name, measure) ->
                    Text("• $name – $measure", modifier = Modifier.padding(start = 16.dp, bottom = 2.dp))
                }

                Spacer(Modifier.height(8.dp))
                Text("Instructions:", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(8.dp))
                Text(d.strInstructions ?: "", modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp))
            }
        } ?: Box(
            Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }
}
