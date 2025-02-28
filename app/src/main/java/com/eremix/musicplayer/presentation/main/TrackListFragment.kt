package com.eremix.musicplayer.presentation.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.eremix.musicplayer.databinding.FragmentTrackListBinding

class TrackListFragment: Fragment() {

    /*
        Здесь будет находиться список треков
     */

    private var _binding: FragmentTrackListBinding? = null
    private val binding: FragmentTrackListBinding
        get() = _binding ?: throw RuntimeException("FragmentTrackListBinding == null")


    private val viewModel by lazy {
        ViewModelProvider(requireActivity())[MainViewModel::class.java]
    }

    private lateinit var adapter: TrackListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTrackListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = viewModel.adapter
        binding.trackRecyclerView.adapter = viewModel.adapter

        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.trackListLD.observe(viewLifecycleOwner) {
            adapter.trackList = it
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}