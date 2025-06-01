package si.uni_lj.fri.pbd.classproject3.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import si.uni_lj.fri.pbd.classproject3.models.RecipeDetailsIM
import si.uni_lj.fri.pbd.classproject3.repository.RecipeRepository
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val repo: RecipeRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val recipeId: String? = savedStateHandle.get<String>("id")
    private val isOnlineFromNav: Boolean = savedStateHandle.get<Boolean>("isOnline") ?: true

    private val _details = MutableStateFlow<RecipeDetailsIM?>(null)
    val details: StateFlow<RecipeDetailsIM?> = _details.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    val ingredientsList: StateFlow<List<Pair<String, String>>> = _details.map { detailsIM ->
        detailsIM?.let { prepareIngredientsList(it) } ?: emptyList()
    }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    init {
        if (recipeId != null) {
            loadRecipeDetailsInternal(recipeId, isOnlineFromNav)
        } else {
            _errorMessage.value = "Recipe ID is missing."
        }
    }

    private fun loadRecipeDetailsInternal(id: String, isOnline: Boolean) = viewModelScope.launch {
        _isLoading.value = true
        _errorMessage.value = null
        _details.value = null
        try {
            _details.value = repo.loadRecipeDetails(id, isOnline = isOnline)
        } catch (e: Exception) {
            _errorMessage.value = "Error loading recipe details: ${e.message}"
        } finally {
            _isLoading.value = false
        }
    }

    fun toggleFavorite() = viewModelScope.launch {
        _details.value?.let { currentDetails ->
            _errorMessage.value = null
            val currentRecipeId = currentDetails.idMeal ?: return@launch

            try {
                repo.toggleFavorite(currentDetails)
                loadRecipeDetailsInternal(currentRecipeId, isOnlineFromNav)
            } catch (e: Exception) {
                _errorMessage.value = "Error toggling favorite: ${e.message}"
            }
        }
    }

    private fun prepareIngredientsList(details: RecipeDetailsIM): List<Pair<String, String>> {
        val ingredients = mutableListOf<Pair<String, String>>()
        val allIngredients = listOf(
            details.strIngredient1, details.strIngredient2, details.strIngredient3, details.strIngredient4,
            details.strIngredient5, details.strIngredient6, details.strIngredient7, details.strIngredient8,
            details.strIngredient9, details.strIngredient10, details.strIngredient11, details.strIngredient12,
            details.strIngredient13, details.strIngredient14, details.strIngredient15, details.strIngredient16,
            details.strIngredient17, details.strIngredient18, details.strIngredient19, details.strIngredient20
        )
        val allMeasures = listOf(
            details.strMeasure1, details.strMeasure2, details.strMeasure3, details.strMeasure4,
            details.strMeasure5, details.strMeasure6, details.strMeasure7, details.strMeasure8,
            details.strMeasure9, details.strMeasure10, details.strMeasure11, details.strMeasure12,
            details.strMeasure13, details.strMeasure14, details.strMeasure15, details.strMeasure16,
            details.strMeasure17, details.strMeasure18, details.strMeasure19, details.strMeasure20
        )

        for (i in 0 until 20) {
            val ingredient = allIngredients[i]
            val measure = allMeasures[i]
            if (!ingredient.isNullOrBlank() && !measure.isNullOrBlank()) {
                ingredients.add(Pair(ingredient, measure))
            } else if (!ingredient.isNullOrBlank() && measure.isNullOrBlank()){
                ingredients.add(Pair(ingredient, "-"))
            }
        }
        return ingredients
    }
}