package si.uni_lj.fri.pbd.classproject3.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import si.uni_lj.fri.pbd.classproject3.models.dto.IngredientDTO
import si.uni_lj.fri.pbd.classproject3.models.dto.RecipeSummaryDTO
import si.uni_lj.fri.pbd.classproject3.repository.RecipeRepository

class SearchViewModel(application: Application?) : AndroidViewModel(application!!) {

    private val TAG = SearchViewModel::class.java.simpleName

    private val recipeRepository: RecipeRepository = RecipeRepository(application)

    private val _recipes = MutableLiveData<List<RecipeSummaryDTO>>()
    val recipes: LiveData<List<RecipeSummaryDTO>> get() = _recipes

    private val _ingredients = MutableLiveData<List<IngredientDTO>>()
    val ingredients: LiveData<List<IngredientDTO>> get() = _ingredients

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> get() = _error

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    init {
        Log.d(TAG, "SearchViewModel initialized")
        fetchAllIngredients()
    }

    private fun fetchAllIngredients() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val result = recipeRepository.getAllIngredients()

                if (result?.isSuccess == true) {
                    _ingredients.value = result.getOrNull() ?: emptyList()
                } else {
                    _error.value = result?.exceptionOrNull()?.message ?: "Failed to fetch ingredients"
                    _ingredients.value = emptyList()
                }
            } catch (e: Exception) {
                _error.value = e.message
                _ingredients.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun searchRecipesByMainIngredient(mainIngredient: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            _recipes.value = emptyList()
            try {
                val result = recipeRepository.getRecipesByMainIngredient(mainIngredient)
                if (result.isSuccess) {
                    _recipes.value = result.getOrNull() ?: emptyList()
                    if (_recipes.value.isNullOrEmpty()) {
                        _error.value = "No recipes found for '$mainIngredient'."
                    }
                } else {
                    _error.value = result.exceptionOrNull()?.message ?: "Failed to fetch recipes"
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "An unexpected error occurred"
            } finally {
                _isLoading.value = false
            }
        }
    }

}