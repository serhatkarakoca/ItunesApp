package com.karakoca.itunesapp.presentation.main

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
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

}