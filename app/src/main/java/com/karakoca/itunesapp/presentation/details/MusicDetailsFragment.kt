package com.karakoca.itunesapp.presentation.details

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.karakoca.core.view.BaseFragment
import com.karakoca.itunesapp.R
import com.karakoca.itunesapp.databinding.FragmentMusicDetailsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MusicDetailsFragment: BaseFragment<FragmentMusicDetailsBinding, MusicDetailsViewModel>(R.layout.fragment_music_details) {
    private val viewModel:MusicDetailsViewModel by viewModels()
    private val args:MusicDetailsFragmentArgs by navArgs()

    override fun init() {
        with(binding){
            details = args.musicDetails
            back.setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }

    private fun getAllMusics(){

    }
}