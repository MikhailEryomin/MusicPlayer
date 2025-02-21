package com.eremix.musicplayer.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.eremix.musicplayer.databinding.CollectionFragmentBinding
import com.eremix.musicplayer.databinding.FragmentTrackPlayerBinding

class CollectionFragment: Fragment() {

    private var _binding: CollectionFragmentBinding? = null
    private val binding: CollectionFragmentBinding
        get() = _binding ?: throw RuntimeException("CollectionFragmentBinding == null")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = CollectionFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}