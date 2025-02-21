package com.eremix.musicplayer.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.eremix.musicplayer.databinding.ActivityTrackBinding

class TrackActivity: AppCompatActivity() {

    private val binding by lazy {
        ActivityTrackBinding.inflate(layoutInflater)
    }

    private val viewModel by lazy {
        ViewModelProvider(this)[TrackListViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val currentTrack = viewModel.currentTrack.value
        currentTrack?.let {
            binding.selTrackTitle.text = currentTrack.title
            binding.selTrackArtist.text = currentTrack.artist
            binding.selTrackDuration.text = currentTrack.durationString
        }
    }


    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, TrackActivity::class.java)
        }
    }

}