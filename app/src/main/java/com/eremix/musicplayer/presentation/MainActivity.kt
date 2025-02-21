package com.eremix.musicplayer.presentation

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.eremix.musicplayer.R
import com.eremix.musicplayer.databinding.ActivityMainBinding

class MainActivity: AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val viewModel by lazy {
        ViewModelProvider(this)[MainViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        launchFragment(TrackListFragment())
        binding.trackView.setOnClickListener {
            viewModel.navigateTo(ScreenState.PLAYER)
        }
        observeViewModel()

        binding.trackListButton.setOnClickListener {
            viewModel.navigateTo(ScreenState.TRACK_LIST)
        }
        binding.collectionButton.setOnClickListener {
            viewModel.navigateTo(ScreenState.COLLECTION)
        }

    }

    private fun changeMiniPlayerVisibility(isVisible: Boolean) {
        if (isVisible) {
            binding.trackViewParent.visibility = View.VISIBLE
        } else {
            binding.trackViewParent.visibility = View.GONE
        }
    }

    private fun launchFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .add(R.id.main_fragment_container, fragment)
            .commit()
        viewModel.navigateTo(
            when(fragment) {
                is TrackListFragment-> ScreenState.TRACK_LIST
                is TrackPlayerFragment-> ScreenState.PLAYER
                is CollectionFragment -> ScreenState.COLLECTION
                else -> {
                    throw RuntimeException("Unknown Fragment class. No screenState exist")
                }
            }
        )
    }

    override fun onBackPressed() {
        if (!viewModel.goBack()) {
            super.onBackPressed()
        }
    }

    private fun observeViewModel() {
        //Mini-player
        viewModel.currentTrack.observe(this) {
            binding.trackTitle.text = it.title
            binding.trackArtist.text = it.artist
            binding.trackDuration.text = it.durationString
        }
        viewModel.screenState.observe(this) {
            when(it) {
                ScreenState.TRACK_LIST -> {
                    launchFragment(TrackListFragment())
                    changeMiniPlayerVisibility(true)
                    binding.bottomDrawer.visibility = View.VISIBLE
                    binding.trackListButton.isEnabled = false
                    binding.collectionButton.isEnabled = true
                }
                ScreenState.PLAYER -> {
                    launchFragment(TrackPlayerFragment())
                    changeMiniPlayerVisibility(false)
                    binding.bottomDrawer.visibility = View.GONE
                }
                ScreenState.COLLECTION -> {
                    launchFragment(CollectionFragment())
                    changeMiniPlayerVisibility(true)
                    binding.bottomDrawer.visibility = View.VISIBLE
                    binding.trackListButton.isEnabled = true
                    binding.collectionButton.isEnabled = false
                }
            }
        }
    }

}