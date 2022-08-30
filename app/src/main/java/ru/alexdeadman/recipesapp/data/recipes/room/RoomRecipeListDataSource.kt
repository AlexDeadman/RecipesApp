package ru.alexdeadman.recipesapp.data.recipes.room

import android.util.Log
import ru.alexdeadman.recipesapp.data.recipes.RecipeListLocalDataSource
import ru.alexdeadman.recipesapp.data.recipes.retrofit.RecipeListResponse

class RoomRecipeListDataSource(private val recipeListDao: RecipeListDao) :
    RecipeListLocalDataSource {

    override suspend fun loadAllRecipes(): List<RecipeEntity> {

        Log.e("", "loadAllRecipes")

        return recipeListDao.loadAllRecipes()
    }

    override suspend fun saveRemoteResponse(response: RecipeListResponse) {
        recipeListDao.saveAllRecipes(
            response.recipes.map { it.toEntity() }
        )
    }
}