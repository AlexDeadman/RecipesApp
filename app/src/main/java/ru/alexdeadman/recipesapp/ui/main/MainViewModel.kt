package ru.alexdeadman.recipesapp.ui.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import ru.alexdeadman.recipesapp.data.recipes.RecipeListRepository
import ru.alexdeadman.recipesapp.ui.state.ListState

class MainViewModel(private val repository: RecipeListRepository) : ViewModel() {

    private val _listStateFlow = MutableStateFlow<ListState?>(null)
    val listStateFlow = _listStateFlow.asStateFlow()

    init {
        fetchRecipes()
    }

    private fun fetchRecipes() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.fetchRecipes().collect {
                _listStateFlow.value = try {
                    if (it.recipes.isEmpty()) ListState.NoItems()
                    else ListState.Loaded(it)
                } catch (e: Exception) {
                    Log.e(TAG, "fetchRecipes: ", e)
                    ListState.Error(e)
                }
            }
        }
    }

    companion object {
        private const val TAG = "MainViewModel"
    }
}