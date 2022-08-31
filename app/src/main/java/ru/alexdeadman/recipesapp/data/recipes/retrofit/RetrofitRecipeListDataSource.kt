package ru.alexdeadman.recipesapp.data.recipes.retrofit

import ru.alexdeadman.recipesapp.data.recipes.RecipeListRemoteDataSource

class RetrofitRecipeListDataSource(private val recipesApi: RecipesApi): RecipeListRemoteDataSource {
    override suspend fun getRecipes(): RecipeListResponse = recipesApi.getRecipes()
}