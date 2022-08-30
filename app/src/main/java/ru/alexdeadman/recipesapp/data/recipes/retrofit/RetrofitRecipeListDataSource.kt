package ru.alexdeadman.recipesapp.data.recipes.retrofit

import android.util.Log
import kotlinx.coroutines.delay
import ru.alexdeadman.recipesapp.data.recipes.RecipeListRemoteDataSource

class RetrofitRecipeListDataSource(private val recipesApi: RecipesApi): RecipeListRemoteDataSource {
    override suspend fun getRecipes(): RecipeListResponse {

        delay(2000)
        Log.e("", "getRecipes")

        return recipesApi.getRecipes()
    }
}