package ru.alexdeadman.recipesapp.ui.list

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import com.mikepenz.fastadapter.diff.FastAdapterDiffUtil
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.filterNotNull
import ru.alexdeadman.recipesapp.R
import ru.alexdeadman.recipesapp.collectOnLifecycle
import ru.alexdeadman.recipesapp.databinding.FragmentListBinding
import ru.alexdeadman.recipesapp.ui.BundleKeys
import ru.alexdeadman.recipesapp.ui.ViewModelFactory
import ru.alexdeadman.recipesapp.ui.state.ListState.*
import javax.inject.Inject

@AndroidEntryPoint
class ListFragment : Fragment() {

    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var itemAdapter: ItemAdapter<ListItem>
    private lateinit var fastAdapter: FastAdapter<ListItem>
    private lateinit var viewModel: ListViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {

            itemAdapter = ItemAdapter()
            fastAdapter = FastAdapter.with(itemAdapter).apply {
                onClickListener = { _, _, item, _ ->
                    findNavController().navigate(
                        R.id.action_listFragment_to_detailsFragment,
                        Bundle().apply {
                            putParcelable(
                                BundleKeys.RECIPE_ITEM,
                                item.recipeItem
                            )
                        }
                    )
                    false
                }
            }

            recyclerView.apply {
                setHasFixedSize(true)
                adapter = fastAdapter

                val isPortrait =
                    resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT

                layoutManager = GridLayoutManager(
                    requireContext(),
                    if (isPortrait) 2 else 3
                )
            }

            viewModel = ViewModelProvider(
                this@ListFragment,
                viewModelFactory
            )[ListViewModel::class.java]

            viewModel.listStateFlow
                .filterNotNull()
                .collectOnLifecycle(viewLifecycleOwner) { state ->
                    when (state) {
                        is Loaded -> {
                            FastAdapterDiffUtil[itemAdapter] =
                                state.result.recipes.map {
                                    ListItem(requireContext(), it)
                                }
                        }
                        is NoItems -> {
                            Toast.makeText(
                                requireContext(),
                                getString(R.string.no_items),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        is Error -> {
                            Toast.makeText(
                                requireContext(),
                                getString(R.string.unknown_error),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}