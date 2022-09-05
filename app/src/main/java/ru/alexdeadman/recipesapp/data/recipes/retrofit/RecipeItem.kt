package ru.alexdeadman.recipesapp.data.recipes.retrofit

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import ru.alexdeadman.recipesapp.data.recipes.room.RecipeEntity

@Parcelize
data class RecipeItem(
    @SerializedName("uuid")
    val uuid: String,

    @SerializedName("name")
    val name: String,

    @SerializedName("images")
    val images: List<String>,

    @SerializedName("lastUpdated")
    val lastUpdated: Int,

    @SerializedName("description")
    val description: String?,

    @SerializedName("instructions")
    val instructions: String,

    @SerializedName("difficulty")
    val difficulty: Int
) : Parcelable {
    fun toEntity(): RecipeEntity = RecipeEntity(
        uuid = this.uuid,
        name = this.name,
        images = this.images,
        lastUpdated = this.lastUpdated,
        description = this.description,
        instructions = this.instructions,
        difficulty = this.difficulty,
    )
}