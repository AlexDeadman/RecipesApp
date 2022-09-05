package ru.alexdeadman.recipesapp.data.recipes.retrofit

import com.google.gson.annotations.SerializedName

data class RecipeListResponse(
    @SerializedName("recipes")
    val recipes: List<RecipeItem>
)