package com.karakoca.itunesapp.presentation.main

import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.karakoca.core.view.BaseActivity
import com.karakoca.itunesapp.R
import com.karakoca.itunesapp.databinding.ActivityMainBinding
import com.karakoca.itunesapp.util.Extensions.isNetworkAvailable
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
        showOfflineMessage()
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }


    private fun showOfflineMessage() {
        if (this.isNetworkAvailable().not()) {
            AlertDialog.Builder(this)
                .setTitle(getString(R.string.network_error))
                .setMessage(getString(R.string.check_internet))
                .setPositiveButton(
                    android.R.string.ok, null
                )
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setCancelable(true)
                .show()
        }
    }
}