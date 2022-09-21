package ru.alexdeadman.recipesapp.ui.list

import ru.alexdeadman.recipesapp.data.recipes.retrofit.RecipeListResponse

@Suppress("CanSealedSubClassBeObject")
sealed class ListState {
    class Loading : ListState()
    class Loaded(val result: RecipeListResponse) : ListState()
    class NoItems : ListState()
}