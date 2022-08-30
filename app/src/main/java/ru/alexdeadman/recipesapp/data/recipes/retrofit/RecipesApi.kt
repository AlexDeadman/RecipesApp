package ru.alexdeadman.recipesapp.data.recipes.retrofit

import retrofit2.http.GET

interface RecipesApi {

    @GET("/recipes.json/")
    suspend fun getRecipes(): RecipeListResponse
}