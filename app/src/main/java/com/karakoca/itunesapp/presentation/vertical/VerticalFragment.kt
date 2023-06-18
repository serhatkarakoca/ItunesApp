package com.karakoca.itunesapp.presentation.vertical

import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.karakoca.core.view.BaseFragment
import com.karakoca.itunesapp.R
import com.karakoca.itunesapp.databinding.FragmentVerticalBinding
import com.karakoca.itunesapp.domain.model.SearchResult
import com.karakoca.itunesapp.presentation.adapter.MusicAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class VerticalFragment: BaseFragment<FragmentVerticalBinding, VerticalViewModel>(R.layout.fragment_vertical) {
    private val viewModel:VerticalViewModel by viewModels()

    private val musicAdapter by lazy { MusicAdapter(emptyList(),MusicAdapter.ItemType.VERTICAL, ::itemClickListener) }


    override fun init() {
        binding.rvVertical.adapter = musicAdapter
        getAllMusics()
    }

    private fun getAllMusics(){
        lifecycleScope.launch {
            viewModel.getMusics().collectLatest {
                musicAdapter.updateList(it)
                binding.totalCount = it.size
            }
        }
    }

    private fun itemClickListener(item: SearchResult) {
        findNavController().navigate(VerticalFragmentDirections.actionVerticalFragmentToMusicDetailsFragment(item))
    }
}