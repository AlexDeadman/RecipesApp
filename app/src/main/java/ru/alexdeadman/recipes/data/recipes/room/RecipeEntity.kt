package ru.alexdeadman.recipes.data.recipes.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.alexdeadman.recipes.data.recipes.room.RecipeEntity.Companion.TABLE_NAME

@Entity(tableName = TABLE_NAME)
data class RecipeEntity(
    @ColumnInfo(name = "uuid")
    @PrimaryKey
    val uuid: String,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "images")
    val images: String,

    @ColumnInfo(name = "last_updated")
    val lastUpdated: Int,

    @ColumnInfo(name = "description")
    val description: String,

    @ColumnInfo(name = "instructions")
    val instructions: String,

    @ColumnInfo(name = "difficulty")
    val difficulty: Int
) {
    companion object {
        const val TABLE_NAME = "recipe_list_entity_table"
    }
}
