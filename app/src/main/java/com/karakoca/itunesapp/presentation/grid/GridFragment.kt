package com.karakoca.itunesapp.presentation.grid

import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.karakoca.core.view.BaseFragment
import com.karakoca.itunesapp.R
import com.karakoca.itunesapp.databinding.FragmentGridBinding
import com.karakoca.itunesapp.domain.model.SearchResult
import com.karakoca.itunesapp.presentation.adapter.MusicAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class GridFragment: BaseFragment<FragmentGridBinding, GridViewModel>(R.layout.fragment_grid) {
    private val viewModel:GridViewModel by viewModels()
    private val musicAdapter by lazy { MusicAdapter(emptyList(), MusicAdapter.ItemType.GRID, ::itemClickListener) }


    override fun init() {
        binding.rvGrid.adapter = musicAdapter
        getAllMusics()
    }

    private fun getAllMusics(){
        lifecycleScope.launch {
            viewModel.getMusics().collectLatest {
                musicAdapter.updateList(it)
            }
        }
    }
    private fun itemClickListener(item: SearchResult) {
        findNavController().navigate(GridFragmentDirections.actionGridFragmentToMusicDetailsFragment(item))
    }
}