package ru.alexdeadman.recipes.ui.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import ru.alexdeadman.recipes.data.recipes.RecipeListRepository
import ru.alexdeadman.recipes.ui.state.ListState

class MainViewModel(private val repository: RecipeListRepository) : ViewModel() {

    val listStateFlow = MutableStateFlow<ListState?>(null)

    init {
        fetchRecipes()
    }

    private fun fetchRecipes() {
        viewModelScope.launch(Dispatchers.IO) {
            listStateFlow.value = try {
                val result = repository.fetchRecipes()
                if (result.recipes.isEmpty()) ListState.NoItems()
                else ListState.Loaded(result)
            } catch (e: Exception) {
                Log.e(TAG, "fetchRecipes: ", e)
                ListState.Error(e)
            }
        }
    }

    companion object {
        private const val TAG = "MainViewModel"
    }
}