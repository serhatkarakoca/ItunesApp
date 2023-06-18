package com.karakoca.itunesapp.presentation.horizontal

import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.karakoca.core.view.BaseFragment
import com.karakoca.itunesapp.R
import com.karakoca.itunesapp.databinding.FragmentHorizontalBinding
import com.karakoca.itunesapp.domain.model.SearchResult
import com.karakoca.itunesapp.presentation.adapter.MusicAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HorizontalFragment :
    BaseFragment<FragmentHorizontalBinding, HorizontalViewModel>(R.layout.fragment_horizontal) {
    private val viewModel: HorizontalViewModel by viewModels()

    private val musicAdapter by lazy {
        MusicAdapter(
            emptyList(),
            MusicAdapter.ItemType.HORIZONTAL,
            ::itemClickListener,
            ::deleteClickListener
        )
    }


    override fun init() {
        binding.rvHorizontal.adapter = musicAdapter
        getAllMusics()
    }

    private fun getAllMusics() {
        lifecycleScope.launch {
            viewModel.getMusics().collectLatest {
                musicAdapter.updateList(it)
            }
        }
    }

    private fun deleteClickListener(trackId: Int) {
        AlertDialog.Builder(requireContext())
            .setMessage(getString(R.string.are_u_sure_delete))
            .setPositiveButton(getString(android.R.string.ok)) { _, _ ->
                viewModel.deleteMusicItem(trackId)
            }
            .setNegativeButton(getString(android.R.string.cancel)) { dialog, _ -> dialog.dismiss() }
            .show()

    }

    private fun itemClickListener(item: SearchResult) {
        findNavController().navigate(
            HorizontalFragmentDirections.actionHorizontalFragmentToMusicDetailsFragment(
                item
            )
        )
    }
}