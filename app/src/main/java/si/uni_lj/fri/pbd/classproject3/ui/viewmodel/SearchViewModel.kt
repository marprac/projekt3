package si.uni_lj.fri.pbd.classproject3.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import si.uni_lj.fri.pbd.classproject3.models.RecipeSummaryIM
import si.uni_lj.fri.pbd.classproject3.repository.RecipeRepository
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
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

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    init {
        viewModelScope.launch {
            _isRefreshing.value = true
            _errorMessage.value = null // Počisti prejšnjo napako
            try {
                _ingredients.value = repo.loadAllIngredients().mapNotNull { it.strIngredient }
            } catch (e: Exception) {
                _errorMessage.value = "Error loading ingredients: ${e.message}"
            } finally {
                _isRefreshing.value = false
            }
        }
    }

    fun selectIngredient(ingredient: String) {
        if (ingredient == _selectedIngredient.value) return
        _selectedIngredient.value = ingredient
        refresh()
    }

    fun refresh() = viewModelScope.launch {
        val now = System.currentTimeMillis()
        if (now - lastPull < 5_000 && !_recipes.value.isEmpty()) return@launch
        lastPull = now

        _isRefreshing.value = true
        _errorMessage.value = null
        try {
            _recipes.value = repo.loadRecipes(_selectedIngredient.value ?: return@launch)
            if (_recipes.value.isEmpty() && _selectedIngredient.value != null) {
                _errorMessage.value = "No recipes found for ${_selectedIngredient.value}."
            }
        } catch (e: Exception) {
            _recipes.value = emptyList()
            _errorMessage.value = "Error loading recipes: ${e.message}"
        } finally {
            _isRefreshing.value = false
        }
    }
}