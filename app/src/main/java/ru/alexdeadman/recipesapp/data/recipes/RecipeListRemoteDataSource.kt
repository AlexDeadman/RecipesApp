package ru.alexdeadman.recipesapp.data.recipes

import ru.alexdeadman.recipesapp.data.recipes.retrofit.RecipeListResponse

interface RecipeListRemoteDataSource {
    suspend fun getRecipes(): RecipeListResponse
}