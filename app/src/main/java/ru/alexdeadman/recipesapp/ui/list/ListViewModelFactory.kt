package ru.alexdeadman.recipesapp.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.alexdeadman.recipesapp.data.recipes.RecipeListRepository

class ListViewModelFactory(
    private val recipeListRepository: RecipeListRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        modelClass.kotlin.constructors.first().call(recipeListRepository)
}