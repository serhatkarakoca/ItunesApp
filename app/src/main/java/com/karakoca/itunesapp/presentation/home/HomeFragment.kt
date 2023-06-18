package com.karakoca.itunesapp.presentation.home

import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.karakoca.core.view.BaseFragment
import com.karakoca.itunesapp.R
import com.karakoca.itunesapp.data.local.ItunesDatabase
import com.karakoca.itunesapp.databinding.FragmentHomeBinding
import com.karakoca.itunesapp.domain.model.SearchResult
import com.karakoca.itunesapp.presentation.home.adapter.ItunesAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment: BaseFragment<FragmentHomeBinding, HomeViewModel>(R.layout.fragment_home) {
    private val viewModel: HomeViewModel by viewModels()

    private val iTunesAdapter by lazy { ItunesAdapter(::itemClickListener) }

    @Inject
    lateinit var itunesDatabase: ItunesDatabase

    override fun init() {
        binding.rvTrack.adapter = iTunesAdapter

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.getHome().collectLatest {
                iTunesAdapter.submitData(it)
            }
        }
    }

    private fun itemClickListener(item: SearchResult) {
        findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToMusicDetailsFragment(item))
    }
}