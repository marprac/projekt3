package si.uni_lj.fri.pbd.classproject3.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import si.uni_lj.fri.pbd.classproject3.database.entity.RecipeDetails
import si.uni_lj.fri.pbd.classproject3.models.Mapper
import si.uni_lj.fri.pbd.classproject3.models.RecipeDetailsIM
import si.uni_lj.fri.pbd.classproject3.models.dto.RecipeDetailsDTO
import si.uni_lj.fri.pbd.classproject3.repository.RecipeRepository

private const val TAG = "DetailsViewModel"

class DetailsViewModel(application: Application?, recipeId: String, fromSearch: Boolean) : AndroidViewModel(application!!) {

    private val recipeRepository: RecipeRepository = RecipeRepository(application)

    private val _recipeUiState = MutableStateFlow(RecipeDetailsIM(
        isLoading = true,
        errorMessage = null,
        isFavorite = false
    ))
    val recipeUiState: StateFlow<RecipeDetailsIM> = _recipeUiState.asStateFlow()

    private val _recipeDetailsDto = MutableStateFlow<RecipeDetailsDTO?>(null)

    private var currentRecipeId: String? = null

    init {
        loadRecipeData(recipeId, fromSearch)
    }

    fun loadRecipeData(recipeId: String, fromSearch: Boolean) {
        currentRecipeId = recipeId
        _recipeUiState.update {
            it.copy(isLoading = true, errorMessage = null)
        }

        viewModelScope.launch {
            if (fromSearch) {
                val recipeDetailsResult = recipeRepository.getRecipeById(recipeId)
                recipeDetailsResult.fold(
                    onSuccess = { recipeDetailsDto ->
                            _recipeDetailsDto.value = recipeDetailsDto
                            // Check if the recipe is already in the database
                            val recipeDetailsEntity: RecipeDetails? = recipeRepository.getRecipeByIdFromDB(recipeId)
                            _recipeUiState.update {
                                it.copy(
                                    isLoading = false,
                                    isFavorite = recipeDetailsEntity?.isFavorite == true,
                                    errorMessage = null
                                )
                            }
                        Log.d(TAG, "Recipe details DTO: $_recipeUiState")

                            val recipeDetailsIm = Mapper.mapRecipeDetailsDtoToRecipeDetailsIm(
                                isFavorite = recipeDetailsEntity?.isFavorite == true,
                                recipeDetailsDto
                            )
                            _recipeUiState.value = recipeDetailsIm.copy(isLoading = false)
                    },
                    onFailure = { exception ->
                        _recipeUiState.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = exception.localizedMessage ?: "Error fetching from API"
                            )
                        }
                    }
                )
            } else {
                val recipeDetailsEntity: RecipeDetails? = recipeRepository.getRecipeByIdFromDB(recipeId)
                if (recipeDetailsEntity != null) {
                    val recipeDetailsIm = Mapper.mapRecipeDetailsToRecipeDetailsIm(true, recipeDetailsEntity)
                    _recipeUiState.value = recipeDetailsIm.copy(isLoading = false)
                } else {
                    _recipeUiState.update {
                        it.copy(isLoading = false, errorMessage = "Recipe not found in DB")
                    }
                }
            }
        }
    }

    fun toggleFavorite() {
        val id = currentRecipeId
        if (id.isNullOrBlank()) {
            _recipeUiState.update { it.copy(errorMessage = "Cannot toggle favorite: Recipe ID missing.") }
            return
        }

        viewModelScope.launch {
            try {
                if (_recipeUiState.value.isFavorite == true) {
                    Log.d(TAG, "Deleting recipe with ID: $id")
                    recipeRepository.deleteRecipeById(id)
                    _recipeUiState.update { it.copy(isFavorite = false) }
                } else {
                    val recipeDetails = Mapper.mapRecipeDetailsDtoToRecipeDetails(true, _recipeDetailsDto.value!!)
                    Log.d(TAG, "Inserting recipe with ID: ${recipeDetails.idMeal}")
                    recipeRepository.insertRecipe(recipeDetails)
                    _recipeUiState.update { it.copy(isFavorite = true) }
                }

            } catch (e: Exception) {
                _recipeUiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = e.localizedMessage ?: "Failed to update favorite"
                    )
                }
            }
        }
    }

    fun refreshRecipeData() {
        val recipeId = currentRecipeId
        if (recipeId != null) {
            loadRecipeData(recipeId, fromSearch = false)
        } else {
            _recipeUiState.update { it.copy(errorMessage = "Cannot refresh: Recipe ID missing.") }
        }
    }

}

class DetailsViewModelFactory(
    private val recipeId: String,
    private val fromSearch: Boolean
) : ViewModelProvider.Factory {
    override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DetailsViewModel(Application(), recipeId, fromSearch) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}