package si.uni_lj.fri.pbd.classproject3.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import si.uni_lj.fri.pbd.classproject3.R
import si.uni_lj.fri.pbd.classproject3.models.dto.RecipeDetailsDTO

@Composable
fun RecipeDetailsScreen(viewModel: RecipeDetailsViewModel, id: String, isOnline: String) {
    val fetchOnline = isOnline
    val isFavorite: Boolean = viewModel.searchResult.observeAsState().value == true

    //If called from search screen get recipe details online, if called from favourites screen get recipe details offline
    if(fetchOnline == "true"){
        viewModel.getRecipesById(id)
    }else{
        viewModel.getRecipesByIdOffline(id)
    }

    //if no recipe selected show message
    if(viewModel.currentId.value == null){
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "No recipe selected", textAlign = TextAlign.Center, fontWeight = FontWeight.Bold, fontSize = 20.sp)
            }
        }
        return
    }

    //Check if in favorites
    viewModel.isRecipeInFavorites(viewModel.currentId.value.toString())


    val isLoading = viewModel.isLoading.observeAsState()
    if(isLoading.value == true){
        //If loading show loading screen
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "Loading...", textAlign = TextAlign.Center, fontWeight = FontWeight.Bold, fontSize = 20.sp)
            }
        }
    }else{
        //Recipe details loaded
        if(id != "" || viewModel.currentId.value != null){
            val recipeDetails = viewModel.getRecipeDetails()
            if(recipeDetails == null){
                //If recipe details are null show message
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "No details found!", textAlign = TextAlign.Center, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                        Text(text = "Check internet connection", textAlign = TextAlign.Center, fontSize = 18.sp)
                    }
                }
            }else{
                //Show recipe details
                RecipeDetailsShow(recipe = recipeDetails!!, viewModel, isFavorite)
            }
        }else{
            //If no recipe selected show message
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
                Text(text = "Select ingredient", textAlign = TextAlign.Center, fontWeight = FontWeight.Bold, fontSize = 20.sp)
            }
        }
    }
}

@Composable
fun RecipeDetailsShow(recipe: RecipeDetailsDTO, viewModel: RecipeDetailsViewModel, isFavorite: Boolean){
    val scrollState = rememberScrollState()
    Column(modifier = Modifier.padding(all = 10.dp).verticalScroll(state = scrollState)) {
        Row {
            Image(
                modifier = Modifier
                    .size(200.dp)
                    .clip(RectangleShape)
                    .padding(start = 10.dp),
                painter = rememberAsyncImagePainter(
                    model = recipe.strMealThumb,
                    placeholder = painterResource(id = R.drawable.ic_launcher_foreground),
                    error = painterResource(id = R.drawable.ic_launcher_foreground)
                ),
                contentDescription = "Recipe picture",
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column(modifier = Modifier.padding(all = 10.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = recipe.strMeal.orEmpty(),
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        modifier = Modifier
                            .padding(bottom = 10.dp)
                            .weight(1f)
                    )
                    if (isFavorite) {
                        Image(
                            modifier = Modifier.size(24.dp).clickable {
                                viewModel.removeRecipeFromFavorites(recipe.idMeal.toString())
                                viewModel.setFavourite(false)
                            },
                            painter = painterResource(id = R.drawable.ic_favourite),
                            contentDescription = "Favorite icon",
                        )
                    } else {
                        Image(
                            modifier = Modifier.size(24.dp).clickable {
                                viewModel.addRecipeToFavorites(recipe)
                                viewModel.setFavourite(true)
                            },
                            painter = painterResource(id = R.drawable.ic_notfavourite),
                            contentDescription = "Favorite icon",
                        )
                    }
                }
                Text(text = recipe.strCategory.orEmpty(), fontSize = 15.sp)
                Text(text = recipe.strArea.orEmpty(), fontSize = 15.sp)
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Ingredients Section
        Text(
            text = "Ingredients:",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            modifier = Modifier.padding(top = 10.dp)
        )

        val ingredients = getIngredientList(recipe)
        for ((ingredient, measure) in ingredients) {
            Text(
                text = "• ${measure.trim()} ${ingredient.trim()}",
                fontSize = 14.sp,
                modifier = Modifier.padding(start = 10.dp, top = 2.dp)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Instructions
        Text(
            text = "Instructions:",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            modifier = Modifier.padding(top = 10.dp)
        )
        Text(
            text = recipe.strInstructions.orEmpty(),
            modifier = Modifier.padding(all = 10.dp)
        )
    }
}


fun getIngredientList(recipe: RecipeDetailsDTO): List<Pair<String, String>> {
    val ingredients = mutableListOf<Pair<String, String>>()

    for (i in 1..20) {
        val ingredient = RecipeDetailsDTO::class.java.getMethod("getStrIngredient$i").invoke(recipe) as? String
        val measure = RecipeDetailsDTO::class.java.getMethod("getStrMeasure$i").invoke(recipe) as? String

        if (!ingredient.isNullOrBlank()) {
            ingredients.add(ingredient to (measure ?: ""))
        }
    }

    return ingredients
}
