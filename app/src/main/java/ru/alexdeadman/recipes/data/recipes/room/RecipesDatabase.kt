package ru.alexdeadman.recipes.data.recipes.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [RecipeEntity::class],
    version = 1,
    exportSchema = true
)
abstract class RecipesDatabase: RoomDatabase() {
    abstract fun recipeListDao(): RecipeListDao
}