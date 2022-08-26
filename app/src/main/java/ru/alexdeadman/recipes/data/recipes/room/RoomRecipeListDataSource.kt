package ru.alexdeadman.recipes.data.recipes.room

import ru.alexdeadman.recipes.data.recipes.RecipeListLocalDataSource

class RoomRecipeListDataSource(private val recipeListDao: RecipeListDao) : RecipeListLocalDataSource {
    override suspend fun loadAllRecipes(): List<RecipeEntity> = recipeListDao.loadAllRecipes()
}