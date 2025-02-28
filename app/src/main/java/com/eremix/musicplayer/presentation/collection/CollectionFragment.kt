package com.eremix.musicplayer.presentation.collection

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import com.eremix.musicplayer.databinding.CollectionFragmentBinding

class CollectionFragment: Fragment()  {

    private var _binding: CollectionFragmentBinding? = null
    private val binding: CollectionFragmentBinding
        get() = _binding ?: throw RuntimeException("CollectionFragmentBinding == null")

    private val adapter = PlaylistAdapter()

    private val viewModel by lazy {
        ViewModelProvider(requireActivity())[CollectionViewModel::class.java]
    }

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

        binding.collNewPlaylistBtn.setOnClickListener {
            val dialogFragment = NewPlaylistDialogFragment.getInstance(viewModel)
            val fragmentManager = requireActivity().supportFragmentManager
            dialogFragment.show(fragmentManager, DIALOG_TAG)
        }

        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        binding.playlistCollectionRw.adapter = adapter
        PlaylistSwipeCallback.apply {
            adapter = this@CollectionFragment.adapter
            viewModel = this@CollectionFragment.viewModel
        }
        val itemTouchHelper = ItemTouchHelper(PlaylistSwipeCallback.playListSwipeCallback)
        itemTouchHelper.attachToRecyclerView(binding.playlistCollectionRw)
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.playlistCollection.observe(viewLifecycleOwner) {
            adapter.playlistCollection = it
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val DIALOG_TAG = "new_playlist"
    }

}