package ru.alexdeadman.recipes.data.recipes.room

import androidx.room.Dao
import androidx.room.Query

@Dao
interface RecipeListDao {
    @Query("select * from ${RecipeEntity.TABLE_NAME}")
    suspend fun loadAllRecipes(): List<RecipeEntity>
}