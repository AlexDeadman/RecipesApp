package ru.alexdeadman.recipesapp.data.recipes.room

import androidx.room.*

@Dao
interface RecipeListDao {

    @Query("select * from ${RecipeEntity.TABLE_NAME}")
    suspend fun loadAllRecipes(): List<RecipeEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    @JvmSuppressWildcards
    suspend fun saveAllRecipes(entities: List<RecipeEntity>)
}