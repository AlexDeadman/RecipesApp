package ru.alexdeadman.recipesapp.ui.list

import android.view.LayoutInflater
import android.view.ViewGroup
import coil.load
import com.mikepenz.fastadapter.binding.AbstractBindingItem
import ru.alexdeadman.recipesapp.R
import ru.alexdeadman.recipesapp.data.recipes.retrofit.RecipeItem
import ru.alexdeadman.recipesapp.databinding.ListItemBinding

class ListItem(private val recipeItem: RecipeItem) : AbstractBindingItem<ListItemBinding>() {

    override val type: Int get() = R.id.list_item_id

    override var identifier: Long
        get() = recipeItem.hashCode().toLong()
        set(value) {
            super.identifier = value
        }

    override fun createBinding(inflater: LayoutInflater, parent: ViewGroup?): ListItemBinding =
        ListItemBinding.inflate(inflater, parent, false)

    override fun bindView(binding: ListItemBinding, payloads: List<Any>) {
        binding.apply {

            textViewCardName.text = recipeItem.name
            textViewCardDescription.text = recipeItem.description
            textViewCardDifficulty.text = recipeItem.difficulty.toString()

            imageViewCardBg.load(recipeItem.images.first()) {
                crossfade(true)
                placeholder(R.drawable.food_placeholder)
                error(R.drawable.food_placeholder)
            }
        }
    }
}