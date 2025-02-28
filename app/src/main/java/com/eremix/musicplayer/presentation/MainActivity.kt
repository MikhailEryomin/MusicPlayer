package com.eremix.musicplayer.presentation

import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.eremix.musicplayer.R
import com.eremix.musicplayer.databinding.ActivityMainBinding
import com.eremix.musicplayer.presentation.collection.CollectionFragment
import com.eremix.musicplayer.presentation.main.MainViewModel
import com.eremix.musicplayer.presentation.main.TrackListFragment
import com.eremix.musicplayer.presentation.main.TrackPlayerFragment

class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val viewModel by lazy {
        ViewModelProvider(this)[MainViewModel::class.java]
    }

    private var isTrackListNotEmpty = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        launchFragment(TrackListFragment())
        binding.trackView.setOnClickListener {
            viewModel.navigateTo(ScreenState.PLAYER)
        }
        observeViewModel()
        checkForPermission()

        binding.trackListButton.setOnClickListener {
            viewModel.navigateTo(ScreenState.TRACK_LIST)
        }
        binding.collectionButton.setOnClickListener {
            viewModel.navigateTo(ScreenState.COLLECTION)
        }
        binding.trackPlayButton.setOnClickListener {
            viewModel.pausePlayer()
        }

        //seekBar
        binding.trackSeekBar.setOnSeekBarChangeListener(object: OnSeekBarChangeListener {
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

    private fun checkForPermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_MEDIA_AUDIO)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_MEDIA_AUDIO), REQUEST_CODE)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
        deviceId: Int
    ) {
        if (requestCode == REQUEST_CODE && grantResults.isNotEmpty()) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                viewModel.setupTrackList()
            } else {
                Log.d("MainActivity", "Permission Denied")
                Toast.makeText(
                    this,
                    "The app cannot get audio until you grant permission",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun changeMiniPlayerVisibility(isVisible: Boolean) {
        if (isVisible) {
            binding.trackViewParent.visibility = View.VISIBLE
        } else {
            binding.trackViewParent.visibility = View.GONE
        }
    }

    private fun changeBottomNavigationVisibility(isVisible: Boolean) {
        if (isVisible) {
            binding.bottomDrawer.visibility = View.VISIBLE
        } else {
            binding.bottomDrawer.visibility = View.GONE
        }
    }

    private fun launchFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .add(R.id.main_fragment_container, fragment)
            .commit()
        viewModel.navigateTo(
            when (fragment) {
                is TrackListFragment -> ScreenState.TRACK_LIST
                is TrackPlayerFragment -> ScreenState.PLAYER
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

    override fun onResume() {
        super.onResume()
        //viewModel.loadTrackList()
    }

    private fun observeViewModel() {
        //Mini-player
        viewModel.currentTrack.observe(this) {
            binding.trackTitle.text = it.title
            binding.trackArtist.text = it.artist
            if (it.thumbnailBitmap != null) {
                binding.trackThumbnail.setImageBitmap(it.thumbnailBitmap)
            } else {
                binding.trackThumbnail.setImageResource(R.drawable.baseline_music_note_24)
            }
            binding.trackSeekBar.max = it.durationInSeconds
            binding.trackDuration.text = it.durationString

        }
        viewModel.listIsNotEmpty.observe(this) {
            isTrackListNotEmpty = it
        }
        viewModel.isPlaying.observe(this) {
            if (it) {
                binding.trackPlayButton.setBackgroundResource(R.drawable.baseline_pause_24)
            } else {
                binding.trackPlayButton.setBackgroundResource(R.drawable.baseline_play_arrow_24)
            }
        }
        viewModel.formattedTime.observe(this) {
            binding.trackCursor.text = it
        }
        viewModel.currentTimeInMillis.observe(this) {
            binding.trackSeekBar.progress = it
        }
        viewModel.screenState.observe(this) {
            when (it) {
                ScreenState.TRACK_LIST -> {
                    launchFragment(TrackListFragment())
                    changeMiniPlayerVisibility(false)
                    if (isTrackListNotEmpty) {
                        changeMiniPlayerVisibility(true)
                    }
                    changeBottomNavigationVisibility(true)
                    binding.trackListButton.isEnabled = false
                    binding.collectionButton.isEnabled = true
                }

                ScreenState.PLAYER -> {
                    launchFragment(TrackPlayerFragment())
                    changeMiniPlayerVisibility(false)
                    changeBottomNavigationVisibility(false)
                }

                ScreenState.COLLECTION -> {
                    launchFragment(CollectionFragment())
                    changeMiniPlayerVisibility(false)
                    if (isTrackListNotEmpty) {
                        changeMiniPlayerVisibility(true)
                    }
                    changeBottomNavigationVisibility(true)
                    binding.trackListButton.isEnabled = true
                    binding.collectionButton.isEnabled = false
                }
            }
        }
    }

    companion object {
        private const val REQUEST_CODE = 111
    }

}