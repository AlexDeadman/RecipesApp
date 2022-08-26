package ru.alexdeadman.recipes.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.filterNotNull
import ru.alexdeadman.recipes.R
import ru.alexdeadman.recipes.collectOnLifecycle
import ru.alexdeadman.recipes.databinding.FragmentMainBinding
import ru.alexdeadman.recipes.ui.ViewModelFactory
import ru.alexdeadman.recipes.ui.state.ListState.*
import javax.inject.Inject

@AndroidEntryPoint
class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var viewModel: MainViewModel

    companion object {
        fun newInstance() = MainFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this, viewModelFactory)[MainViewModel::class.java]

        viewModel.listStateFlow
            .filterNotNull()
            .collectOnLifecycle(viewLifecycleOwner) {
                binding.message.text = when (it) {
                    is Loaded -> { it.result.recipes.toString() }
                    is NoItems -> { getString(R.string.no_items) }
                    is Error -> { getString(R.string.unknown_error) }
                }
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}