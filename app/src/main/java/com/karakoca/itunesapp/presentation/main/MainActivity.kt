package com.karakoca.itunesapp.presentation.main

import androidx.core.view.isVisible
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.karakoca.core.view.BaseActivity
import com.karakoca.itunesapp.R
import com.karakoca.itunesapp.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity: BaseActivity<ActivityMainBinding , MainViewModel>(R.layout.activity_main) {

    private val navHostFragment by lazy { supportFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment }
    private val navController by lazy { navHostFragment.navController }


    override fun init() {
        with(binding) {
            bottomNavView.setupWithNavController(navController)
        }

        setSupportActionBar(binding.toolbar)
        navController.addOnDestinationChangedListener { navigation, destination, bundle ->
            when (destination.id) {
                R.id.musicDetailsFragment -> binding.toolbar.isVisible = false
                else -> binding.toolbar.isVisible = true
            }

        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }


}