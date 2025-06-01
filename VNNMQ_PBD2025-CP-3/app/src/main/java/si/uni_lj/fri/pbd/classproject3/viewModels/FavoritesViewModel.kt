
package si.uni_lj.fri.pbd.classproject3.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import si.uni_lj.fri.pbd.classproject3.data.RecipeRepository
import si.uni_lj.fri.pbd.classproject3.models.RecipeSummaryIM

class FavoritesViewModel(
    application: Application
) : AndroidViewModel(application) {
    private val repo = RecipeRepository(getApplication())

    val favorites: StateFlow<List<RecipeSummaryIM>> =
        repo.getFavoriteRecipes()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
            )
}