package ru.alexdeadman.recipesapp.ui.details

import android.content.ContentValues
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import androidx.navigation.navGraphViewModels
import com.daimajia.slider.library.SliderTypes.BaseSliderView
import com.daimajia.slider.library.SliderTypes.DefaultSliderView
import com.daimajia.slider.library.Transformers.BaseTransformer
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import com.stfalcon.imageviewer.StfalconImageViewer
import ru.alexdeadman.recipesapp.R
import ru.alexdeadman.recipesapp.data.recipes.retrofit.RecipeItem
import ru.alexdeadman.recipesapp.databinding.FragmentDetailsBinding
import ru.alexdeadman.recipesapp.showToast
import ru.alexdeadman.recipesapp.ui.BundleKeys


class DetailsFragment : Fragment() {

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!

    private lateinit var imageViewer: StfalconImageViewer<String>
    private lateinit var picasso: Picasso

    private lateinit var recipeItem: RecipeItem
    private val images: List<String> get() = recipeItem.images

    private val viewModel: DetailsViewModel by navGraphViewModels(R.id.detailsFragment)

    companion object {
        const val IS_DIALOG_SHOWN = "is_dialog_shown"
        const val CURRENT_POSITION = "current_position"

        private const val TAG = "DetailsFragment"
    }

    private var isDialogShown: Boolean = false
    private var currentPosition: Int = 0

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

            viewModel.currentRecipeItem = requireArguments().getParcelable(BundleKeys.RECIPE_ITEM)!!
            recipeItem = viewModel.currentRecipeItem

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

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)

        savedInstanceState?.let {
            isDialogShown = it.getBoolean(IS_DIALOG_SHOWN)
            currentPosition = it.getInt(CURRENT_POSITION)
        }

        if (isDialogShown) {
            openImageViewer(currentPosition)
        }
    }

    private fun openImageViewer(startPosition: Int) {

        val imageOverlay = ImageOverlayView(requireContext()).apply {
            onDownloadClick = {
                picasso
                    .load(images[imageViewer.currentPosition()])
                    .into(
                        object : Target {
                            override fun onBitmapLoaded(bitmap: Bitmap, from: Picasso.LoadedFrom?) {
                                saveToGallery(bitmap)
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
            .withStartPosition(startPosition)
            .withImageChangeListener { currentPosition = it }
            .withDismissListener { isDialogShown = false }
            .show(!isDialogShown)

        currentPosition = startPosition
        isDialogShown = true
    }

    private fun saveToGallery(bitmap: Bitmap) {

        val contentResolver = requireContext().contentResolver
        val title = "${recipeItem.name} ${imageViewer.currentPosition() + 1}"

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {

                val contentValues = ContentValues().apply {
                    put(MediaStore.MediaColumns.DISPLAY_NAME, "$title.jpg")
                    put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
                    put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
                }

                val imageUri = contentResolver.insert(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    contentValues
                )

                val outputStream = contentResolver.openOutputStream(imageUri!!)

                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)

            } else {
                @Suppress("DEPRECATION")
                MediaStore.Images.Media.insertImage(
                    contentResolver,
                    bitmap,
                    title,
                    ""
                )
            }
            showToast(R.string.image_saved)

        } catch (e: Exception) {
            showToast(R.string.cant_save_image)
            Log.e(TAG, "saveToGallery: $e")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        imageViewer.dismiss()
        _binding = null
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putBoolean(IS_DIALOG_SHOWN, isDialogShown)
        outState.putInt(CURRENT_POSITION, currentPosition)

        super.onSaveInstanceState(outState)
    }
}