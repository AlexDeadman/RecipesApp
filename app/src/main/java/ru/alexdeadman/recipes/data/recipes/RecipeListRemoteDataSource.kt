package ru.alexdeadman.recipes.data.recipes

import ru.alexdeadman.recipes.data.recipes.retrofit.RecipeListResponse

interface RecipeListRemoteDataSource {
    suspend fun getRecipes(): RecipeListResponse
}