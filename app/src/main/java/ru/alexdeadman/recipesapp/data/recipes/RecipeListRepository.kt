package ru.alexdeadman.recipesapp.data.recipes

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.alexdeadman.recipesapp.data.recipes.retrofit.RecipeListResponse

class RecipeListRepository(
    private val recipeListLocalDataSource: RecipeListLocalDataSource,
    private val recipeListRemoteDataSource: RecipeListRemoteDataSource
) {
    fun fetchRecipes(): Flow<RecipeListResponse> = flow {
        emit(
            RecipeListResponse(
                recipeListLocalDataSource.loadAllRecipes().map { it.toItem() }
            )
        )
        emit(
            recipeListRemoteDataSource.getRecipes().also {
                recipeListLocalDataSource.saveRemoteResponse(it)
            }
        )
    }
}