package com.karakoca.itunesapp.presentation.home

import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import com.karakoca.core.extension.observe
import com.karakoca.core.view.BaseFragment
import com.karakoca.itunesapp.R
import com.karakoca.itunesapp.databinding.FragmentHomeBinding
import com.karakoca.itunesapp.domain.model.SearchResult
import com.karakoca.itunesapp.presentation.home.adapter.ItunesAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding, HomeViewModel>(R.layout.fragment_home) {
    private val viewModel: HomeViewModel by viewModels()

    private val iTunesAdapter by lazy { ItunesAdapter(::itemClickListener) }

    private var searchJob: Job? = null
    private var pagingJob: Job? = null

    override fun onStart() {
        super.onStart()
        initQuery()
    }

    override fun init() {
        observe(viewModel.searchTerm) {
            getHomeData()
            viewModel.clearItems()
        }
        binding.rvTrack.adapter = iTunesAdapter

        getHomeData()

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                searchJob?.cancel()
                searchJob = lifecycleScope.launch {
                    delay(1000)
                    viewModel.searchTerm.value =
                        if (newText.isNullOrEmpty().not()) newText else "jack johnson"
                }
                return true
            }

        })
    }

    private fun initQuery() {
        lifecycleScope.launch {
            iTunesAdapter.loadStateFlow
                // Only emit when REFRESH LoadState for RemoteMediator changes.
                .distinctUntilChangedBy { it.refresh }
                // Only react to cases where Remote REFRESH completes i.e., NotLoading.
                .filter { it.refresh is LoadState.NotLoading }
                .collectLatest {
                    binding.tvEmpty.isVisible = viewModel.getLocalDataSize() == 0
                }
        }
    }

    private fun getHomeData() {
        pagingJob?.cancel()
        pagingJob = viewLifecycleOwner.lifecycleScope.launch {
            viewModel.getHome().collectLatest {
                iTunesAdapter.submitData(it)
            }
        }
    }

    private fun itemClickListener(item: SearchResult) {
        findNavController().navigate(
            HomeFragmentDirections.actionHomeFragmentToMusicDetailsFragment(
                item
            )
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        pagingJob = null
        searchJob = null
    }
}