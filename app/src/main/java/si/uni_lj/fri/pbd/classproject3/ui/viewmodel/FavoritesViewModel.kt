package si.uni_lj.fri.pbd.classproject3.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import si.uni_lj.fri.pbd.classproject3.models.RecipeSummaryIM
import si.uni_lj.fri.pbd.classproject3.repository.RecipeRepository

class FavoritesViewModel(repo: RecipeRepository) : ViewModel() {
    val favorites: StateFlow<List<RecipeSummaryIM>> =
        repo.favoriteRecipes()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
}