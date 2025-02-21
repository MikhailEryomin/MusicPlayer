package com.eremix.musicplayer.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
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
    }

    private fun observeViewModel() {
        viewModel.currentTrack.observe(viewLifecycleOwner) {
            binding.selTrackTitle.text = it.title
            binding.selTrackArtist.text = it.artist
            binding.selTrackDuration.text = it.durationString
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}