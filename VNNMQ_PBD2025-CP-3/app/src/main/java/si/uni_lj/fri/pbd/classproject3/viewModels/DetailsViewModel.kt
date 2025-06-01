// File: app/src/main/java/si/uni_lj/fri/pbd/classproject3/viewModels/DetailsViewModel.kt
package si.uni_lj.fri.pbd.classproject3.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import si.uni_lj.fri.pbd.classproject3.data.RecipeRepository
import si.uni_lj.fri.pbd.classproject3.models.RecipeDetailsIM

class DetailsViewModel(
    private val repo: RecipeRepository,
    private val recipeId: String,
    private val localOnly: Boolean
) : ViewModel() {

    data class UiState(
        val details: RecipeDetailsIM? = null,
        val isFavorite: Boolean = false,
        val isLoading: Boolean = true,
        val error: String? = null
    )

    private val _state = MutableStateFlow(UiState())
    val state: StateFlow<UiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            try {
                val detailsData: RecipeDetailsIM = if (localOnly) {
                    repo.getRecipeDetailsFromCache(recipeId)
                        ?: throw IllegalStateException("No cached details for recipe $recipeId")
                } else {
                    repo.getRecipeDetails(recipeId)
                }

                repo.getFavoriteRecipes()
                    .map { favs -> favs.any { it.idMeal == recipeId } }
                    .collect { favFlag ->
                        _state.value = UiState(
                            details    = detailsData,
                            isFavorite = favFlag,
                            isLoading  = false
                        )
                    }
            } catch (e: Exception) {
                _state.value = UiState(
                    isLoading = false,
                    error     = e.message ?: "An unexpected error occurred"
                )
            }
        }
    }

    fun toggleFavorite() {
        viewModelScope.launch {
            repo.toggleFavorite(recipeId)
        }
    }

    class Factory(
        private val repo: RecipeRepository,
        private val recipeId: String,
        private val localOnly: Boolean
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(DetailsViewModel::class.java)) {
                return DetailsViewModel(repo, recipeId, localOnly) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class: $modelClass")
        }
    }
}