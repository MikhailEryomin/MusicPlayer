package com.eremix.musicplayer.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.eremix.musicplayer.R
import com.eremix.musicplayer.databinding.ActivityTrackListBinding

class TrackListActivity: AppCompatActivity() {

    private val binding by lazy {
        ActivityTrackListBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupTrackList()
    }

    private fun setupTrackList() {
        TODO("Not yet implemented")
    }

}