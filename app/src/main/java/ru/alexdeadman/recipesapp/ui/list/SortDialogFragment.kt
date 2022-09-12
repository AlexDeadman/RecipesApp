package ru.alexdeadman.recipesapp.ui.list

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import dagger.hilt.android.AndroidEntryPoint
import ru.alexdeadman.recipesapp.R
import ru.alexdeadman.recipesapp.ui.list.SortDialogFragment.SortProperty.*
import javax.inject.Inject

@AndroidEntryPoint
class SortDialogFragment : DialogFragment() {

    enum class SortProperty {
        NONE,
        NAME_ASC,
        NAME_DESC,
        DATE_ASC,
        DATE_DESC
    }

    companion object {
        val SORT_OPTIONS = mapOf(
            NONE to 0,
            NAME_ASC to 1,
            NAME_DESC to 2,
            DATE_ASC to 3,
            DATE_DESC to 4
        )
    }

    @Inject
    lateinit var listViewModelFactory: ListViewModelFactory

    private lateinit var viewModel: ListViewModel

    var onChoice: () -> Unit = {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(
            requireActivity(),
            listViewModelFactory
        )[ListViewModel::class.java]
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.sort)
            .setSingleChoiceItems(
                R.array.sort_options,
                viewModel.sortOption.value
            ) { _, which ->
                viewModel.sortOption = SORT_OPTIONS.entries.single { it.value == which }
                onChoice()
                dismiss()
            }
            .create()
}