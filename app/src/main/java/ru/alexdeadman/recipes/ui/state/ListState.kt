package ru.alexdeadman.recipes.ui.state

import ru.alexdeadman.recipes.data.recipes.retrofit.RecipeListResponse

@Suppress("CanSealedSubClassBeObject")
sealed class ListState {
    class Loaded(val result: RecipeListResponse) : ListState()
    class NoItems : ListState()
    class Error(val throwable: Throwable) : ListState()
}