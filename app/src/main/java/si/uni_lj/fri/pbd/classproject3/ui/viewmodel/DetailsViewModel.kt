package si.uni_lj.fri.pbd.classproject3.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import si.uni_lj.fri.pbd.classproject3.models.RecipeDetailsIM
import si.uni_lj.fri.pbd.classproject3.repository.RecipeRepository

class DetailsViewModel(
    private val repo: RecipeRepository
) : ViewModel() {

    private val _details = MutableStateFlow<RecipeDetailsIM?>(null)
    val details: StateFlow<RecipeDetailsIM?> = _details.asStateFlow()

    fun load(id: String) = viewModelScope.launch {
        _details.value = repo.loadRecipeDetails(id)
    }

    fun toggleFavorite() = viewModelScope.launch {
        _details.value?.let { repo.toggleFavorite(it) }
        // reload to update star icon
        _details.value?.idMeal?.let { load(it) }
    }
}
