package ru.alexdeadman.recipesapp.ui.list

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import com.mikepenz.fastadapter.diff.FastAdapterDiffUtil
import com.mikepenz.fastadapter.utils.ComparableItemListImpl
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import org.apache.commons.text.similarity.CosineDistance
import ru.alexdeadman.recipesapp.R
import ru.alexdeadman.recipesapp.collectOnLifecycle
import ru.alexdeadman.recipesapp.databinding.FragmentListBinding
import ru.alexdeadman.recipesapp.ui.BundleKeys
import ru.alexdeadman.recipesapp.ui.details.DetailsFragment
import ru.alexdeadman.recipesapp.ui.details.DetailsViewModel
import ru.alexdeadman.recipesapp.ui.list.ListState.*
import ru.alexdeadman.recipesapp.ui.list.SortDialogFragment.SortProperty
import javax.inject.Inject

@AndroidEntryPoint
class ListFragment : Fragment() {

    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var listViewModelFactory: ListViewModelFactory

    private val listViewModel: ListViewModel by viewModels({ requireActivity() }) { listViewModelFactory }
    private val detailsViewModel: DetailsViewModel by navGraphViewModels(R.id.detailsFragment)

    private var isSimilarityList: Boolean = false

    private lateinit var itemListImpl: ComparableItemListImpl<ListItem>
    private lateinit var itemAdapter: ItemAdapter<ListItem>
    private lateinit var fastAdapter: FastAdapter<ListItem>

    private lateinit var searchView: SearchView

    private val sortComparator: Comparator<ListItem>
        get() = Comparator { l: ListItem, r: ListItem ->
            when (listViewModel.sortOption.key) {
                SortProperty.NONE -> 0
                SortProperty.NAME_ASC -> l.recipeItem.name.compareTo(r.recipeItem.name)
                SortProperty.NAME_DESC -> r.recipeItem.name.compareTo(l.recipeItem.name)
                SortProperty.DATE_ASC -> r.recipeItem.lastUpdated.compareTo(l.recipeItem.lastUpdated)
                SortProperty.DATE_DESC -> l.recipeItem.lastUpdated.compareTo(r.recipeItem.lastUpdated)
            }
        }

    private val sortDialog = SortDialogFragment().apply {
        onChoice = { itemListImpl.withComparator(sortComparator) }
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

        isSimilarityList = parentFragment is DetailsFragment

        if (!isSimilarityList) {
            setupMenu()
        }

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

            listViewModel.listStateFlow
                .collectOnLifecycle(viewLifecycleOwner) { state ->
                    when (state) {
                        is Loading -> {
                            progressBar.visibility = View.VISIBLE
                        }
                        is Loaded -> {
                            progressBar.visibility = View.GONE
                            textViewMessage.text = ""

                            val recipes = state.result.recipes

                            FastAdapterDiffUtil[itemAdapter] =
                                if (isSimilarityList) {
                                    if (detailsViewModel.similarItems == null) {
                                        val current = detailsViewModel.currentRecipeItem
                                        val cosineDistance = CosineDistance()

                                        detailsViewModel.similarItems = recipes
                                            .filter { it != current }
                                            .map {
                                                it to cosineDistance.apply(
                                                    current.mainText,
                                                    it.mainText
                                                )
                                            }
                                            .filter { it.second in 0.0..0.75 }
                                    }
                                    detailsViewModel.similarItems!!
                                        .map {
                                            ListItem(it.first, picasso).apply {
                                                distance = it.second
                                            }
                                        }
                                        .also {
                                            if (it.isEmpty()) textViewMessage.setText(R.string.not_found)
                                        }
                                } else {
                                    recipes.map { ListItem(it, picasso) }
                                }
                        }
                        is NoItems -> {
                            progressBar.visibility = View.GONE
                            textViewMessage.setText(R.string.list_empty)
                        }
                    }
                    Log.e(TAG, "state:  ${state::class.simpleName}")
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

                    listViewModel.searchQuery?.let {
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
                            listViewModel.searchQuery = searchView.query.toString()
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
        itemListImpl = ComparableItemListImpl(
            if (isSimilarityList) Comparator { l, r -> l.distance.compareTo(r.distance) }
            else sortComparator
        )

        itemAdapter = ItemAdapter(itemListImpl).apply {
            itemFilter.filterPredicate = { listItem, constraint ->
                listItem.recipeItem.fullText.contains(constraint!!, true)
            }
        }

        fastAdapter = FastAdapter.with(itemAdapter).apply {
            onClickListener = { _, _, listItem, _ ->
                val destination =
                    if (isSimilarityList) {
                        R.id.action_detailsFragment_self
                    } else {
                        listViewModel.searchQuery = searchView.query.toString()
                        R.id.action_listFragment_to_detailsFragment
                    }

                findNavController().navigate(
                    destination,
                    Bundle().apply {
                        putParcelable(
                            BundleKeys.RECIPE_ITEM,
                            listItem.recipeItem
                        )
                    }
                )

                false
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        if (view != null && !isSimilarityList) {
            listViewModel.searchQuery = searchView.query.toString()
        }
        super.onSaveInstanceState(outState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val TAG = "ListFragment"
    }
}