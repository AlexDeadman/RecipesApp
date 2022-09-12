package ru.alexdeadman.recipesapp.data.recipes.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.alexdeadman.recipesapp.data.recipes.retrofit.RecipeItem
import ru.alexdeadman.recipesapp.data.recipes.room.RecipeEntity.Companion.TABLE_NAME

@Entity(tableName = TABLE_NAME)
data class RecipeEntity(
    @ColumnInfo(name = "uuid")
    @PrimaryKey
    val uuid: String,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "images")
    val images: List<String>,

    @ColumnInfo(name = "last_updated")
    val lastUpdated: Int,

    @ColumnInfo(name = "description")
    val description: String?,

    @ColumnInfo(name = "instructions")
    val instructions: String,

    @ColumnInfo(name = "difficulty")
    val difficulty: Int
) {
    companion object {
        const val TABLE_NAME = "recipe_list_entity_table"
    }

    fun toItem(): RecipeItem = RecipeItem(
        uuid = this.uuid,
        name = this.name,
        images = this.images,
        lastUpdated = this.lastUpdated,
        description = this.description,
        instructions = this.instructions,
        difficulty = this.difficulty,
    )
}
