package si.uni_lj.fri.pbd.classproject3.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import si.uni_lj.fri.pbd.classproject3.data.RecipeRepository
import si.uni_lj.fri.pbd.classproject3.models.RecipeSummaryIM
import java.net.UnknownHostException

class SearchViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val repository = RecipeRepository(getApplication())
    private var lastDownloadMillis: Long = 0L

    data class UiState(
        val ingredients: List<String> = emptyList(),
        val selectedIngredient: String? = null,
        val recipes: List<RecipeSummaryIM> = emptyList(),
        val isLoading: Boolean = false,
        val errorMessage: String? = null
    )

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    init {
        loadIngredients()
    }

    private fun loadIngredients() = viewModelScope.launch {
        _uiState.update { it.copy(isLoading = true, errorMessage = null) }
        runCatching { repository.getIngredients() }
            .onSuccess { list ->
                _uiState.update { it.copy(ingredients = list, isLoading = false) }
                if (list.isNotEmpty()) selectIngredient(list[0])
            }
            .onFailure { e ->
                _uiState.update { it.copy(errorMessage = friendlyError(e), isLoading = false) }
            }
    }

    fun selectIngredient(ingredient: String) = viewModelScope.launch {
        _uiState.update { it.copy(
            selectedIngredient = ingredient,
            isLoading = true,
            errorMessage = null,
            recipes = emptyList()
        ) }
        runCatching { repository.getRecipesByIngredient(ingredient) }
            .onSuccess { list ->
                lastDownloadMillis = System.currentTimeMillis()
                _uiState.update { it.copy(recipes = list, isLoading = false) }
            }
            .onFailure { e -> _uiState.update { it.copy(errorMessage = friendlyError(e), isLoading = false) } }
    }

    fun refresh() {
        val now = System.currentTimeMillis()

        val shouldThrottle =
            uiState.value.errorMessage == null &&
                    now - lastDownloadMillis < 5_000

        if (shouldThrottle) return
        val selected = uiState.value.selectedIngredient
        if (selected == null) {
            loadIngredients()
        } else {
            selectIngredient(selected)
        }
    }
    private fun friendlyError(t: Throwable): String =
        if (t is UnknownHostException)
            "Could not connect to the server. Please check your internet connection."
        else
            t.localizedMessage ?: "Unexpected error"
}
