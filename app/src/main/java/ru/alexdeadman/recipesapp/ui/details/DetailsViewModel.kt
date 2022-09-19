package ru.alexdeadman.recipesapp.ui.details

import androidx.lifecycle.ViewModel
import ru.alexdeadman.recipesapp.data.recipes.retrofit.RecipeItem

class DetailsViewModel : ViewModel() {
    lateinit var currentRecipeItem: RecipeItem
    var similarItems: List<Pair<RecipeItem, Double>>? = null
}