package ru.alexdeadman.recipesapp.data.recipes.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.alexdeadman.recipesapp.data.converters.StringListConverter

@Database(
    entities = [RecipeEntity::class],
    version = 1,
    exportSchema = true
)
@TypeConverters(StringListConverter::class)
abstract class RecipesDatabase: RoomDatabase() {
    abstract fun recipeListDao(): RecipeListDao
}