package ru.alexdeadman.recipesapp.ui.list

import android.content.res.Configuration
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import com.mikepenz.fastadapter.diff.FastAdapterDiffUtil
import com.mikepenz.fastadapter.utils.ComparableItemListImpl
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.filterNotNull
import ru.alexdeadman.recipesapp.R
import ru.alexdeadman.recipesapp.collectOnLifecycle
import ru.alexdeadman.recipesapp.databinding.FragmentListBinding
import ru.alexdeadman.recipesapp.showToast
import ru.alexdeadman.recipesapp.ui.BundleKeys
import ru.alexdeadman.recipesapp.ui.list.ListState.*
import ru.alexdeadman.recipesapp.ui.list.SortDialogFragment.SortProperty
import javax.inject.Inject

@AndroidEntryPoint
class ListFragment : Fragment() {

    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var listViewModelFactory: ListViewModelFactory

    private lateinit var itemListImpl: ComparableItemListImpl<ListItem>

    private lateinit var itemAdapter: ItemAdapter<ListItem>
    private lateinit var fastAdapter: FastAdapter<ListItem>

    private lateinit var viewModel: ListViewModel

    private lateinit var searchView: SearchView

    private val comparator: Comparator<ListItem>
        get() = Comparator(
            when (viewModel.sortOption.key) {
                SortProperty.NONE ->
                    { _, _ -> 0 }
                SortProperty.NAME_ASC ->
                    { l, r -> l.recipeItem.name.compareTo(r.recipeItem.name) }
                SortProperty.NAME_DESC ->
                    { l, r -> r.recipeItem.name.compareTo(l.recipeItem.name) }
                SortProperty.DATE_ASC ->
                    { l, r -> r.recipeItem.lastUpdated.compareTo(l.recipeItem.lastUpdated) }
                SortProperty.DATE_DESC ->
                    { l, r -> l.recipeItem.lastUpdated.compareTo(r.recipeItem.lastUpdated) }
            }
        )

    private val sortDialog = SortDialogFragment().apply {
        onChoice = { itemListImpl.withComparator(comparator) }
    }

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

        viewModel = ViewModelProvider(
            requireActivity(),
            listViewModelFactory
        )[ListViewModel::class.java]

        setupMenu()
        initAdapters()

        binding.apply {

            recyclerView.apply {
                setHasFixedSize(true)
                adapter = fastAdapter

                val orientation = resources.configuration.orientation
                layoutManager = GridLayoutManager(
                    requireContext(),
                    if (orientation == Configuration.ORIENTATION_PORTRAIT) 2 else 3
                )
            }

            val picasso = Picasso.with(requireContext())

            viewModel.listStateFlow
                .filterNotNull()
                .collectOnLifecycle(viewLifecycleOwner) { state ->
                    when (state) {
                        is Loaded -> {
                            FastAdapterDiffUtil[itemAdapter] =
                                state.result.recipes.map { ListItem(it, picasso) }

                            fastAdapter.withSavedInstanceState(savedInstanceState)
                        }
                        is NoItems -> showToast(getString(R.string.no_items))
                        is Error -> showToast(getString(R.string.unknown_error))
                    }
                }
        }
    }

    private fun setupMenu() {
        requireActivity().addMenuProvider(
            object : MenuProvider {
                override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {

                    menuInflater.inflate(R.menu.menu_list_fragment, menu)

                    val searchMenuItem = menu.findItem(R.id.menu_action_search)

                    searchView = (searchMenuItem.actionView as SearchView).apply {
                        setOnQueryTextListener(
                            object : SearchView.OnQueryTextListener {
                                override fun onQueryTextChange(newText: String): Boolean {
                                    itemAdapter.filter(newText)
                                    return true
                                }

                                override fun onQueryTextSubmit(query: String): Boolean = false
                            }
                        )
                    }

                    viewModel.searchQuery?.let {
                        if (it.isNotBlank()) {
                            searchMenuItem.expandActionView()
                            searchView.apply {
                                setQuery(it, false)
                                clearFocus()
                            }
                        }
                    }
                }

                override fun onMenuItemSelected(menuItem: MenuItem): Boolean =
                    when (menuItem.itemId) {
                        R.id.menu_action_sort -> {
                            viewModel.searchQuery = searchView.query.toString()
                            sortDialog.show(childFragmentManager, null)
                            true
                        }
                        R.id.menu_action_search -> true
                        else -> false
                    }
            },
            viewLifecycleOwner,
            Lifecycle.State.RESUMED
        )
    }

    private fun initAdapters() {
        itemListImpl = ComparableItemListImpl(comparator)

        itemAdapter = ItemAdapter(itemListImpl).apply {
            itemFilter.filterPredicate = { item, constraint ->
                item.recipeItem
                    .let { listOf(it.name, it.description, it.instructions).joinToString() }
                    .contains(constraint!!, true)
            }
        }

        fastAdapter = FastAdapter.with(itemAdapter).apply {
            onClickListener = { _, _, item, _ ->
                viewModel.searchQuery = searchView.query.toString()
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
    }

    override fun onSaveInstanceState(outState: Bundle) {
        if (view != null) {
            viewModel.searchQuery = searchView.query.toString()
            super.onSaveInstanceState(fastAdapter.saveInstanceState(outState))
        } else {
            super.onSaveInstanceState(outState)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val TAG = "ListFragment"
    }
}