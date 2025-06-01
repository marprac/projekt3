package si.uni_lj.fri.pbd.classproject3.screens

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import si.uni_lj.fri.pbd.classproject3.database.RecipeRepository
import si.uni_lj.fri.pbd.classproject3.models.RecipeSummaryIM
import si.uni_lj.fri.pbd.classproject3.models.dto.IngredientsDTO
import si.uni_lj.fri.pbd.classproject3.models.dto.RecipesByIngredientDTO

class FavouritesViewModel(application: Application?) : AndroidViewModel(application!!) {
    //All favourited recipes
    var recipesFavourited: MutableLiveData<List<RecipeSummaryIM>>
    private val repository: RecipeRepository

    //Get all favourited recipes
    fun getFavourited() {
        repository.getRecipesFavourited()
        Log.d("FavouritesViewModel", "getFavourited")
    }

    init {
        repository = RecipeRepository(application)
        recipesFavourited = repository.recipesFavourited
    }
}
