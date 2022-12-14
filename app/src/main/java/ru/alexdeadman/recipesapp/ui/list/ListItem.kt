package ru.alexdeadman.recipesapp.ui.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.mikepenz.fastadapter.binding.AbstractBindingItem
import com.squareup.picasso.Picasso
import ru.alexdeadman.recipesapp.R
import ru.alexdeadman.recipesapp.data.recipes.retrofit.RecipeItem
import ru.alexdeadman.recipesapp.databinding.ListItemBinding

class ListItem(
    val recipeItem: RecipeItem,
    private val picasso: Picasso
) : AbstractBindingItem<ListItemBinding>() {

    override val type: Int get() = R.id.list_item_id

    override var identifier: Long
        get() = recipeItem.hashCode().toLong()
        set(value) {
            super.identifier = value
        }

    var distance: Double = 0.0

    override fun createBinding(inflater: LayoutInflater, parent: ViewGroup?): ListItemBinding =
        ListItemBinding.inflate(inflater, parent, false)

    override fun bindView(binding: ListItemBinding, payloads: List<Any>) {

        binding.apply {

            textViewName.text = recipeItem.name
            textViewDescription.text = recipeItem.description
            textViewDifficulty.text = recipeItem.difficulty.toString()

            cardDifficulty.setCardBackgroundColor(
                ContextCompat.getColor(
                    root.context,
                    when (recipeItem.difficulty) {
                        1 -> R.color.diff_1
                        2 -> R.color.diff_2
                        3 -> R.color.diff_3
                        4 -> R.color.diff_4
                        5 -> R.color.diff_5
                        else -> R.color.black
                    }
                )
            )

            picasso
                .load(recipeItem.images.first())
                .placeholder(R.drawable.food_placeholder_grid)
                .into(imageViewBg)
        }
    }
}