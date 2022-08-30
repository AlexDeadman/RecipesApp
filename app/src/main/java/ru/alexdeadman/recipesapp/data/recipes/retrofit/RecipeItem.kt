/*
Copyright (c) 2022 Kotlin Data Classes Generated from JSON powered by http://www.json2kotlin.com

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

For support, please feel free to contact me at https://www.linkedin.com/in/syedabsar */

package ru.alexdeadman.recipesapp.data.recipes.retrofit

import com.google.gson.annotations.SerializedName
import ru.alexdeadman.recipesapp.data.recipes.room.RecipeEntity

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
) {
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