package ru.alexdeadman.recipes.data.recipes

import ru.alexdeadman.recipes.data.recipes.retrofit.RecipeListResponse

class RecipeListRepository(
    private val recipeListLocalDataSource: RecipeListLocalDataSource,
    private val recipeListRemoteDataSource: RecipeListRemoteDataSource
) {
    suspend fun fetchRecipes(): RecipeListResponse = recipeListRemoteDataSource.getRecipes()
}