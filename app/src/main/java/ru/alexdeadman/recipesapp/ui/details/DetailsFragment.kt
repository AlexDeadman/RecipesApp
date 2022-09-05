package ru.alexdeadman.recipesapp.ui.details

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import com.daimajia.slider.library.SliderTypes.BaseSliderView
import com.daimajia.slider.library.SliderTypes.DefaultSliderView
import com.daimajia.slider.library.Transformers.BaseTransformer
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import com.stfalcon.imageviewer.StfalconImageViewer
import ru.alexdeadman.recipesapp.R
import ru.alexdeadman.recipesapp.data.recipes.retrofit.RecipeItem
import ru.alexdeadman.recipesapp.databinding.FragmentDetailsBinding
import ru.alexdeadman.recipesapp.ui.BundleKeys

class DetailsFragment : Fragment() {

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!

    private lateinit var imageViewer: StfalconImageViewer<String>
    private lateinit var picasso: Picasso
    private lateinit var images: List<String>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {

            imageViewer = StfalconImageViewer.Builder<String>(
                requireContext(),
                emptyList()
            ) { _, _ -> }.build()

            picasso = Picasso.with(requireContext())

            val recipeItem = requireArguments().getParcelable<RecipeItem>(BundleKeys.RECIPE_ITEM)!!
            images = recipeItem.images

            images.map { url ->
                sliderLayout.addSlider(
                    DefaultSliderView(requireContext())
                        .image(url)
                        .setScaleType(BaseSliderView.ScaleType.CenterCrop)
                        .empty(R.drawable.food_placeholder_details)
                        .setOnSliderClickListener { openImageViewer(sliderLayout.currentPosition) }
                )
            }

            if (images.size < 2) {
                sliderLayout.apply {
                    stopAutoCycle()
                    pagerIndicator.visibility = View.INVISIBLE
                    setPagerTransformer(
                        false,
                        object : BaseTransformer() {
                            override fun onTransform(view: View?, position: Float) {}
                        }
                    )
                }
            }

            textViewName.text = recipeItem.name

            textViewDescription.text = recipeItem.description.let {
                if (it.isNullOrBlank()) getString(R.string.no_description) else it
            }

            textViewInstructions.text = HtmlCompat.fromHtml(
                recipeItem.instructions,
                HtmlCompat.FROM_HTML_MODE_COMPACT
            )

            textViewDifficulty.text = getString(R.string.difficulty, recipeItem.difficulty)
        }
    }

    private fun openImageViewer(position: Int) {

        val imageOverlay = ImageOverlayView(requireContext()).apply {
            onDownloadClick = {
                picasso.load(
                    images[imageViewer.currentPosition()]
                ).into(
                    object : Target {
                        override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
                            // TODO save to gallery
                        }

                        override fun onBitmapFailed(errorDrawable: Drawable?) {}
                        override fun onPrepareLoad(placeHolderDrawable: Drawable?) {}
                    }
                )

            }
            onBackClick = {
                imageViewer.close()
            }
        }

        imageViewer = StfalconImageViewer.Builder(
            requireContext(),
            images
        ) { imageView, url ->
            picasso.load(url).into(imageView)
        }
            .withOverlayView(imageOverlay)
            .withStartPosition(position)
            .show()
    }

    override fun onPause() {
        super.onPause()
        imageViewer.dismiss()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}