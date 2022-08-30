package ru.alexdeadman.recipesapp.data.recipes

import ru.alexdeadman.recipesapp.data.recipes.retrofit.RecipeListResponse
import ru.alexdeadman.recipesapp.data.recipes.room.RecipeEntity

interface RecipeListLocalDataSource {
    suspend fun loadAllRecipes(): List<RecipeEntity>
    suspend fun saveRemoteResponse(response: RecipeListResponse)
}