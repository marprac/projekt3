package si.uni_lj.fri.pbd.classproject3.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import si.uni_lj.fri.pbd.classproject3.R
import si.uni_lj.fri.pbd.classproject3.models.dto.RecipeSummaryDTO

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(viewModel: SearchViewModel, navigateToRecipeDetails: (recipeId: String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    //Get options from viewModel
    val options = viewModel.ingredients.observeAsState()
    if(options.value?.ingredients?.size == null){
        //If options are null show message
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "No ingredients found!", textAlign = TextAlign.Center, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                Text(text = "Check connection and reopen the app", textAlign = TextAlign.Center, fontSize = 18.sp)
            }
          }
        return
    }

    Column (modifier = Modifier.padding(all = 1.dp)){
        //Dropdown selection box for ingredients
        ExposedDropdownMenuBox(
            modifier = Modifier.padding(all = 10.dp),
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                readOnly = true,
                value = viewModel.getSelectedIngredient(),
                onValueChange = {},
                label = { Text(text="Search")},
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                },
                colors = OutlinedTextFieldDefaults.colors(),
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                options.value?.ingredients?.forEach { selectionOption ->

                    DropdownMenuItem(
                        text = { Text(text = selectionOption.strIngredient.toString()) },
                        onClick = {
                            expanded = false
                            //Set selected ingredient
                            viewModel.setSelectedIngredient(selectionOption.strIngredient.toString())
                            //Search for results
                            viewModel.getRecipesByIngredient(selectionOption.strIngredient.toString())
                        }
                    )
                }
            }
        }
        Spacer(modifier = Modifier.width(8.dp))
        //Get search results
        val recipesByIngredient = viewModel.recipesByIngredient.observeAsState()

        if(recipesByIngredient.value?.recipes?.size != null){
            //If there are results show them
            var isRefreshing = viewModel.isRefreshing.observeAsState()
            val state = rememberPullToRefreshState()
            val onRefresh: () -> Unit = {
                viewModel.getRecipesByIngredient(viewModel.getSelectedIngredient())
            }
            PullToRefreshBox(
                isRefreshing = isRefreshing.value == true,
                onRefresh = onRefresh,
                state = state
            ) {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(recipesByIngredient.value?.recipes?.size ?: 0) { recipe ->
                        //Card for each recipe
                        RecipeCard(
                            recipe = recipesByIngredient.value?.recipes?.get(recipe),
                            navigateToRecipeDetails
                        )
                    }
                }
            }
        }else {
            //If there are no results
            if(viewModel.getSelectedIngredient() != ""){
                //If ingredient is selected but no results are found show message
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
                    Text(text = "No recipes found", textAlign = TextAlign.Center, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                }
            }else{
                //If ingredient is not selected show message
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
                    Text(text = "Select ingredient", textAlign = TextAlign.Center, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                }
            }

        }
    }
}

//Composable for recipe card
@Composable
fun RecipeCard(recipe: RecipeSummaryDTO?, onClick: (recipeId: String) -> Unit){
    Row(modifier = Modifier
        .padding(all = 8.dp)
        .fillMaxWidth()
        .clickable(onClick = {
            //Navigate to recipe details screen
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
        Box(modifier = Modifier
            .size(height = 40.dp, width = 2000.dp)
            .fillMaxWidth(), contentAlignment = Alignment.CenterStart) {
            Text(textAlign = TextAlign.Center, text = recipe?.strMeal.toString())
        }
    }
}