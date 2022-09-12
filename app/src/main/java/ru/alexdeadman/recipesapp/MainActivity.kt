package ru.alexdeadman.recipesapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import dagger.hilt.android.AndroidEntryPoint
import ru.alexdeadman.recipesapp.databinding.ActivityMainBinding

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {

            setSupportActionBar(appbar.toolbar)

            val navController = fragmentContainerView.getFragment<NavHostFragment>().navController

            NavigationUI.run {
                setupWithNavController(
                    appbar.toolbar,
                    navController
                )
                setupActionBarWithNavController(
                    this@MainActivity,
                    navController,
                )
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}