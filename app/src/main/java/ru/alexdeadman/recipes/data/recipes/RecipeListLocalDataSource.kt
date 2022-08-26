package ru.alexdeadman.recipes.data.recipes

import ru.alexdeadman.recipes.data.recipes.retrofit.RecipeListResponse
import ru.alexdeadman.recipes.data.recipes.room.RecipeEntity

interface RecipeListLocalDataSource {
    suspend fun loadAllRecipes(): List<RecipeEntity>
    suspend fun saveRemoteResponse(response: RecipeListResponse)
}