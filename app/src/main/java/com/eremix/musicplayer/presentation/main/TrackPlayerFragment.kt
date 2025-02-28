package com.eremix.musicplayer.presentation.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.eremix.musicplayer.R
import com.eremix.musicplayer.databinding.FragmentTrackPlayerBinding

class TrackPlayerFragment: Fragment() {

    private var _binding: FragmentTrackPlayerBinding? = null
    private val binding: FragmentTrackPlayerBinding
        get() = _binding ?: throw RuntimeException("FragmentTrackPlayerBinding == null")

    private val viewModel by lazy {
        ViewModelProvider(requireActivity())[MainViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTrackPlayerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
        binding.selTrackPlayButton.setOnClickListener {
            viewModel.pausePlayer()
        }
        binding.selTrackNextButton.setOnClickListener {
            viewModel.playNext()
        }
        binding.selTrackPreviousButton.setOnClickListener {
            viewModel.playPrev()
        }
        binding.returnButton.setOnClickListener {
            viewModel.goBack()
        }

        binding.selTrackSeekBar.setOnSeekBarChangeListener(object: OnSeekBarChangeListener {
            override fun onProgressChanged(
                seekBar: SeekBar?,
                progress: Int,
                fromUser: Boolean
            ) {
                if (fromUser) {
                    viewModel.seekTo(progress)
                }
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }
            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })
    }

    private fun observeViewModel() {
        viewModel.currentTrack.observe(viewLifecycleOwner) {
            binding.selTrackTitle.text = it.title
            binding.selTrackArtist.text = it.artist

            if (it.thumbnailBitmap != null) {
                binding.selTrackThumbnail.setImageBitmap(it.thumbnailBitmap)
            } else {
                binding.selTrackThumbnail.setImageResource(R.drawable.baseline_music_note_24)
            }

            binding.selTrackSeekBar.max = it.durationInSeconds
            binding.selTrackDuration.text = it.durationString
        }
        viewModel.formattedTime.observe(viewLifecycleOwner) {
            binding.selTrackCursor.text = it
        }
        viewModel.currentTimeInMillis.observe(viewLifecycleOwner) {
            binding.selTrackSeekBar.progress = it
        }
        viewModel.isPlaying.observe(viewLifecycleOwner) {
            if (it) {
                binding.selTrackPlayButton.setBackgroundResource(R.drawable.baseline_pause_24)
            } else {
                binding.selTrackPlayButton.setBackgroundResource(R.drawable.baseline_play_arrow_24)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}