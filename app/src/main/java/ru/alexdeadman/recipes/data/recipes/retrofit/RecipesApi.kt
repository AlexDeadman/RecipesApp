package ru.alexdeadman.recipes.data.recipes.retrofit

import retrofit2.http.GET
import ru.alexdeadman.recipes.data.recipes.retrofit.RecipeListResponse

interface RecipesApi {

    @GET("/recipes.json/")
    suspend fun getRecipes(): RecipeListResponse
}