package ru.alexdeadman.recipesapp.ui.list

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ru.alexdeadman.recipesapp.data.recipes.RecipeListRepository

class ListViewModel(private val repository: RecipeListRepository) : ViewModel() {

    private val _listStateFlow = MutableStateFlow<ListState>(ListState.Loading())
    val listStateFlow = _listStateFlow.asStateFlow()

    var sortOption = SortDialogFragment.SORT_OPTIONS.entries.first()
    var searchQuery: String? = null

    init {
        viewModelScope.launch(Dispatchers.IO) {
            repository.fetchRecipes()
                .onEach {
                    _listStateFlow.value =
                        if (it.recipes.isEmpty()) ListState.NoItems()
                        else ListState.Loaded(it)
                }
                .catch {
                    Log.e(TAG, it.toString())
                }
                .collect()
        }
    }

    companion object {
        private const val TAG = "ListViewModel"
    }
}