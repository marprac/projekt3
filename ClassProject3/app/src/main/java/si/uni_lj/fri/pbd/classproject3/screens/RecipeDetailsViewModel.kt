package si.uni_lj.fri.pbd.classproject3.screens

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import si.uni_lj.fri.pbd.classproject3.database.RecipeRepository
import si.uni_lj.fri.pbd.classproject3.models.dto.RecipeDetailsDTO
import si.uni_lj.fri.pbd.classproject3.models.dto.RecipesByIdDTO

class RecipeDetailsViewModel(application: Application?) : AndroidViewModel(application!!) {
    //Recipe Details
    private val recipesById: LiveData<RecipeDetailsDTO?>
    //Current recipe id of the loaded recipe details
    var currentId = MutableLiveData<String>()
    //If recipe details are loading
    var isLoading = MutableLiveData<Boolean>()
    //If recipe is in favorites
    var searchResult = MutableLiveData<Boolean>()

    private val repository: RecipeRepository

    //Get recipe details by id online for search screen
    fun getRecipesById(id: String){
        if(currentId.value ==  id || isLoading.value == true || id == ""){
            return
        }
        repository.resetRecipesById()
        currentId.value = id
        isLoading.value = true
        repository.getRecipesById(id)
    }

    //Get recipe details by id offline for favourites screen
    fun getRecipesByIdOffline(id: String){
        if(currentId.value ==  id || isLoading.value == true || id == ""){
            return
        }
        currentId.value = id
        isLoading.value = true
        repository.getRecipesByIdOffline(id)
    }

    //Get recipe details
    fun getRecipeDetails(): RecipeDetailsDTO? {
        return recipesById.value
    }

    //Check if recipe is in favorites
    fun isRecipeInFavorites(id: String){
        repository.isRecipeInFavorites(id)
    }

    //Add recipe to favorites
    fun addRecipeToFavorites(recipe: RecipeDetailsDTO){
        repository.addRecipeToFavorites(recipe)
    }

    //Remove recipe from favorites
    fun removeRecipeFromFavorites(id: String){
        repository.removeRecipeFromFavorites(id)
    }

    //Set favourite variable to true or false
    fun setFavourite(isFavorite: Boolean){
        searchResult.postValue(isFavorite)
    }

    init {
        repository = RecipeRepository(application)
        recipesById = repository.recipesById
        isLoading = repository.isLoadingId
        searchResult = repository.searchResult
    }
}