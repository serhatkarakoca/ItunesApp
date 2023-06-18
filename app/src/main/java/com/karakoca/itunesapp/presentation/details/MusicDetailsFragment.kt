package com.karakoca.itunesapp.presentation.details

import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.karakoca.core.view.BaseFragment
import com.karakoca.itunesapp.App.Companion.mCacheDataSourceFactory
import com.karakoca.itunesapp.R
import com.karakoca.itunesapp.databinding.FragmentMusicDetailsBinding
import com.karakoca.itunesapp.util.createEnterTransition
import com.karakoca.itunesapp.util.createReturnTransition
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MusicDetailsFragment: BaseFragment<FragmentMusicDetailsBinding, MusicDetailsViewModel>(R.layout.fragment_music_details) {
    private val viewModel: MusicDetailsViewModel by viewModels()

    private val args: MusicDetailsFragmentArgs by navArgs()
    private var isPlaying = false
    private var playerPosition = 0L

    override fun init() {
        enterTransition = createEnterTransition()
        exitTransition = createReturnTransition()

        with(binding) {
            details = args.musicDetails
            details?.previewUrl?.let {
                val exoPlayer = getExoPlayer(it)
                if (isPlaying) {
                    exoPlayer.play()
                } else {
                    exoPlayer.pause()
                }
                exoPlayer.seekTo(playerPosition)
                exoPlayer.playWhenReady = isPlaying
                exoAudioTrack.player = exoPlayer
            }

            if (details?.previewUrl.isNullOrEmpty()) {
                exoAudioTrack.visibility = View.GONE
            }

            exoAudioTrack.player?.addListener(object : Player.Listener {
                override fun onIsPlayingChanged(isPlaying: Boolean) {
                    super.onIsPlayingChanged(isPlaying)
                    this@MusicDetailsFragment.isPlaying = isPlaying
                }
            })
            back.setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }

    private fun getExoPlayer(previewUrl: String): ExoPlayer {
        val exoPlayer = ExoPlayer.Builder(requireContext()).build()
        val mediaItem: MediaItem = MediaItem.fromUri(previewUrl)
        val mediaSource =
            ProgressiveMediaSource.Factory(mCacheDataSourceFactory).createMediaSource(mediaItem)
        exoPlayer.setMediaSource(mediaSource)
        exoPlayer.prepare()
        return exoPlayer
    }

    override fun onPause() {
        super.onPause()
        binding.exoAudioTrack.player?.pause()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.exoAudioTrack.player?.release()
    }
}