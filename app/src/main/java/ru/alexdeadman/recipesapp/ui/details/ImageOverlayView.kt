package ru.alexdeadman.recipesapp.ui.details

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.coordinatorlayout.widget.CoordinatorLayout
import ru.alexdeadman.recipesapp.databinding.ViewImageOverlayBinding

class ImageOverlayView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
) : CoordinatorLayout(context, attrs) {

    var onDownloadClick: () -> Unit = {}
    var onBackClick: () -> Unit = {}

    init {
        ViewImageOverlayBinding.inflate(
            LayoutInflater.from(context),
            this,
            true
        ).apply {
            buttonDownload.setOnClickListener { onDownloadClick() }
            buttonBack.setOnClickListener { onBackClick() }
        }
    }
}