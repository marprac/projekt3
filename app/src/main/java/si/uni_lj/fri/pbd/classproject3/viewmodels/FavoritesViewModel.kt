package si.uni_lj.fri.pbd.classproject3.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import si.uni_lj.fri.pbd.classproject3.models.Mapper
import si.uni_lj.fri.pbd.classproject3.models.RecipeSummaryIM
import si.uni_lj.fri.pbd.classproject3.repository.RecipeRepository


data class FavoritesUiState(
    val isLoading: Boolean = true,
    val favoriteRecipes: List<RecipeSummaryIM>,
    val errorMessage: String? = null
)

class FavoritesViewModel(application: Application?) : AndroidViewModel(application!!) {
    private val recipeRepository: RecipeRepository = RecipeRepository(application)

    val uiState: StateFlow<FavoritesUiState> = recipeRepository.getFavoriteRecipes()
        .map { recipeDetailsList ->
            FavoritesUiState(
                isLoading = false,
                favoriteRecipes = recipeDetailsList.map { Mapper.mapRecipeDetailsToRecipeSummaryIm(it) }
            )
        }
        .stateIn(viewModelScope, SharingStarted.Lazily, FavoritesUiState(isLoading = true, emptyList()))
}