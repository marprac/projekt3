package si.uni_lj.fri.pbd.classproject3.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import si.uni_lj.fri.pbd.classproject3.R
import si.uni_lj.fri.pbd.classproject3.models.RecipeSummaryIM

@Composable
fun FavouritesScreen(viewModel: FavouritesViewModel, navigateToRecipeDetails: (recipeId: String) -> Unit) {
    //Get all favourited recipes
    viewModel.getFavourited()
    //Observe viewmodel variable
    val recipesFavourited = viewModel.recipesFavourited.observeAsState()

    if (recipesFavourited.value?.size != null) {
        //If there are favourited recipes, show them
        LazyColumn {
            items(recipesFavourited.value?.size ?: 0) { recipe ->
                RecipeCardF(recipe = recipesFavourited.value?.get(recipe), navigateToRecipeDetails)
            }
        }
    } else {
        //If there are no favourited recipes, show a message
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(
                text = "No recipes found",
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
        }
    }
}

    //Recipe card for favourites
    @Composable
    fun RecipeCardF(recipe: RecipeSummaryIM?, onClick: (recipeId: String) -> Unit) {
        Row(modifier = Modifier
            .padding(all = 8.dp)
            .fillMaxWidth()
            .clickable(onClick = {
                recipe?.idMeal?.let { id -> onClick(id) }
            })) {
            Image(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape),
                painter = rememberAsyncImagePainter(
                    model = recipe?.strMealThumb,
                    placeholder = painterResource(id = R.drawable.ic_launcher_foreground),
                    error = painterResource(id = R.drawable.ic_launcher_foreground)
                ),
                contentDescription = "Recipe picture",
            )
            Spacer(modifier = Modifier.width(8.dp))
            Box(
                modifier = Modifier
                    .size(height = 40.dp, width = 2000.dp)
                    .fillMaxWidth(),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(textAlign = TextAlign.Center, text = recipe?.strMeal.toString())
            }
        }
    }