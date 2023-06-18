package com.karakoca.itunesapp.presentation.details

import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.karakoca.core.view.BaseFragment
import com.karakoca.itunesapp.R
import com.karakoca.itunesapp.databinding.FragmentVerticalBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MusicDetailsFragment: BaseFragment<FragmentVerticalBinding, MusicDetailsViewModel>(R.layout.fragment_music_details) {
    private val viewModel:MusicDetailsViewModel by viewModels()


    override fun init() {

    }

    private fun getAllMusics(){
        lifecycleScope.launch {
            viewModel.getMusics().collectLatest {

            }
        }
    }
}