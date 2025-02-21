package com.eremix.musicplayer.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.eremix.musicplayer.databinding.ActivityTrackListBinding

class TrackListActivity: AppCompatActivity() {

    private val binding by lazy {
        ActivityTrackListBinding.inflate(layoutInflater)
    }

    private val viewModel by lazy {
        ViewModelProvider(this)[TrackListViewModel::class.java]
    }

    private lateinit var adapter: TrackListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        adapter = viewModel.adapter
        binding.trackRecyclerView.adapter = adapter
        binding.trackView.setOnClickListener {
            launchTrackActivity()
        }
        observeViewModel()
    }

    private fun launchTrackActivity() {
        startActivity(TrackActivity.newIntent(this))
    }

    private fun observeViewModel() {
        viewModel.trackList.observe(this) {
            adapter.trackList = it
        }
        viewModel.currentTrack.observe(this) {
            binding.trackTitle.text = it.title
            binding.trackArtist.text = it.artist
            binding.trackDuration.text = it.durationString
        }
    }

}