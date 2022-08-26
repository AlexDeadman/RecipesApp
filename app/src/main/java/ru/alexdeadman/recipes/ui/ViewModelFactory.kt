package ru.alexdeadman.recipes.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.alexdeadman.recipes.data.recipes.RecipeListRepository

class ViewModelFactory(
    private val recipeListRepository: RecipeListRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        modelClass.kotlin.constructors.first().call(recipeListRepository)
}