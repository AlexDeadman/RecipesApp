package ru.alexdeadman.recipes.data.recipes.retrofit

import ru.alexdeadman.recipes.data.recipes.RecipeListRemoteDataSource

class RetrofitRecipeListDataSource(private val recipesApi: RecipesApi): RecipeListRemoteDataSource {
    override suspend fun getRecipes(): RecipeListResponse = recipesApi.getRecipes()
}