package si.uni_lj.fri.pbd.classproject3.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import si.uni_lj.fri.pbd.classproject3.repository.RecipeRepository
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val repository: RecipeRepository
) : ViewModel() {

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        preloadData()
    }

    fun preloadData() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                repository.loadAllIngredients()
            } catch (e: Exception) {
                _error.value = "Failed to preload initial data: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}