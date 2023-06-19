package com.karakoca.itunesapp.presentation.home

import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
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
        observe(viewModel.state, ::onStateChanged)
        binding.rvTrack.adapter = iTunesAdapter

        getHomeData()

        binding.swipeRefreshLayout.setOnRefreshListener {
            getHomeData()
        }

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                searchJob?.cancel()

                searchJob = viewLifecycleOwner.lifecycleScope.launch {
                    delay(1000)
                    val searchTerm = if (!newText.isNullOrEmpty()) newText else "jack johnson"
                    if (searchTerm != viewModel.searchTerm) viewModel.searchMusic(searchTerm)
                }

                return false
            }
        })
    }

    private fun initQuery() {
        lifecycleScope.launch {
            iTunesAdapter.loadStateFlow
                // Only emit when REFRESH LoadState for RemoteMediator changes.
                .distinctUntilChangedBy { it.refresh }
                // Only react to cases where Remote REFRESH completes i.e., NotLoading.
                .filter { it.refresh is LoadState.NotLoading || it.refresh is LoadState.Loading }
                .collectLatest {
                    if (it.refresh is LoadState.NotLoading) {
                        binding.tvEmpty.isVisible = viewModel.getLocalDataSize() == 0
                        binding.progressBar.isVisible = false
                        if (!binding.rvTrack.isVisible) {
                            binding.rvTrack.isVisible = true
                        }
                    } else {
                        binding.tvEmpty.isVisible = false
                        binding.progressBar.isVisible = true
                        if (binding.rvTrack.isVisible) binding.rvTrack.isVisible = false
                    }
                }

        }
    }

    private fun getHomeData() {
        pagingJob?.cancel()
        pagingJob = lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.getHome().collectLatest {
                    iTunesAdapter.submitData(it)
                    binding.swipeRefreshLayout.isRefreshing = false
                }
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

    private fun onStateChanged(state: HomeViewModel.State) {
        when (state) {
            is HomeViewModel.State.OnQueryChanged -> {
                getHomeData()
                viewModel.clearState()
            }

            else -> Unit
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        pagingJob = null
        searchJob = null
    }
}