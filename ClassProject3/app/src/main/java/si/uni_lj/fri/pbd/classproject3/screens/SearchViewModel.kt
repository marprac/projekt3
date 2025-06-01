package si.uni_lj.fri.pbd.classproject3.screens

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.LiveData
import si.uni_lj.fri.pbd.classproject3.database.RecipeRepository
import si.uni_lj.fri.pbd.classproject3.database.entity.RecipeDetails
import si.uni_lj.fri.pbd.classproject3.models.dto.IngredientsDTO
import si.uni_lj.fri.pbd.classproject3.models.dto.RecipesByIngredientDTO

class SearchViewModel(application: Application?) : AndroidViewModel(application!!) {
    //List of ingredients
    val ingredients: LiveData<IngredientsDTO?>
    //List of recipes by ingredient
    var recipesByIngredient : LiveData<RecipesByIngredientDTO?>
    //Selected ingredient
    private var selectedIngredient = MutableLiveData<String>()
    //If ingredients are loading
    var isLoading = MutableLiveData<Boolean>()
    //Start time of app
    var startTime: Long
    //If recipes are refreshing
    var isRefreshing = MutableLiveData<Boolean>()

    private val repository: RecipeRepository

    //Get recipes by ingredient
    fun getRecipesByIngredient(ingredient: String){
        repository.getRecipesByIngredient(ingredient)
        Log.d("SearchViewModel", "getRecipesByIngredient: $ingredient")
    }

    //Get selected ingredient
    fun getSelectedIngredient(): String {
        return selectedIngredient.value ?: ""
    }

    //Set selected ingredient
    fun setSelectedIngredient(ingredient: String) {
        selectedIngredient.value = ingredient
    }

    //Get all ingredients
    fun getAllIngredients(){
        Log.d("SearchViewModel", "getAllIngredients called")
        repository.getAllIngredients()
    }

    init {
        repository = RecipeRepository(application)
        ingredients = repository.ingredients
        recipesByIngredient = repository.recipesByIngredient
        isLoading = repository.ingredientsLoading
        isRefreshing = repository.isRefreshing
        startTime = 0
    }
}