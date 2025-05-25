package si.uni_lj.fri.pbd.classproject3.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import si.uni_lj.fri.pbd.classproject3.models.RecipeSummaryIM
import si.uni_lj.fri.pbd.classproject3.repository.RecipeRepository

class SearchViewModel(
    private val repo: RecipeRepository
) : ViewModel() {

    private var lastPull = 0L

    private val _ingredients = MutableStateFlow<List<String>>(emptyList())
    val ingredients: StateFlow<List<String>> = _ingredients.asStateFlow()

    private val _selectedIngredient = MutableStateFlow<String?>(null)
    val selectedIngredient: StateFlow<String?> = _selectedIngredient.asStateFlow()

    private val _recipes = MutableStateFlow<List<RecipeSummaryIM>>(emptyList())
    val recipes: StateFlow<List<RecipeSummaryIM>> = _recipes.asStateFlow()

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

    init {
        viewModelScope.launch {
            _ingredients.value = repo.loadAllIngredients().map { it.strIngredient }
        }
    }

    fun selectIngredient(ingredient: String) {
        if (ingredient == _selectedIngredient.value) return
        _selectedIngredient.value = ingredient
        refresh()
    }

    fun refresh() = viewModelScope.launch {
        val now = System.currentTimeMillis()
        if (now - lastPull < 5_000) return@launch
        lastPull = now

        _isRefreshing.value = true
        _recipes.value = repo.loadRecipes(_selectedIngredient.value ?: return@launch)
        _isRefreshing.value = false
    }
}
