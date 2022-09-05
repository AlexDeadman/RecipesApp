package ru.alexdeadman.recipesapp.ui.list

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import ru.alexdeadman.recipesapp.data.recipes.RecipeListRepository
import ru.alexdeadman.recipesapp.ui.state.ListState

class ListViewModel(private val repository: RecipeListRepository) : ViewModel() {

    private val _listStateFlow = MutableStateFlow<ListState?>(null)
    val listStateFlow = _listStateFlow.asStateFlow()

    init {
        fetchRecipes()
    }

    private fun fetchRecipes() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.fetchRecipes()
                .catch {
                    // TODO handle
                }.collect {
                    _listStateFlow.value = try {
                        if (it.recipes.isEmpty()) ListState.NoItems()
                        else ListState.Loaded(it)
                    } catch (e: Exception) {
                        Log.e(TAG, e.toString())
                        ListState.Error(e)
                    }
                }
        }
    }

    companion object {
        private val TAG = this::class.simpleName
    }
}