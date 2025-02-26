package com.eremix.musicplayer.presentation

import android.app.Application
import android.media.MediaPlayer
import android.net.Uri
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.eremix.musicplayer.data.TrackListRepositoryImpl
import com.eremix.musicplayer.data.TrackMapper
import com.eremix.musicplayer.domain.GetTrackListUseCase
import com.eremix.musicplayer.domain.Track
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.util.Stack


class MainViewModel(private val application: Application): AndroidViewModel(application) {

    //will be fixed with dagger soon
    private val repository = TrackListRepositoryImpl

    val adapter = TrackListAdapter(application)

    private val _trackList = MutableLiveData<List<Track>>()
    val trackList: LiveData<List<Track>>
        get() = _trackList

    private val _currentTrack = MutableLiveData<Track>()
    val currentTrack: LiveData<Track>
        get() = _currentTrack

    private val _isPlaying = MutableLiveData(false)
    val isPlaying: LiveData<Boolean>
        get() = _isPlaying

    private val _currentTimeInSeconds = MutableLiveData<Int>(0)
    val currentTimeInMillis: LiveData<Int>
        get() = _currentTimeInSeconds

    private val mapper = TrackMapper()
    val formattedTime: LiveData<String>
        get() = _currentTimeInSeconds.map {
        mapper.durationInSecondsToString(it)
    }

    private val _screenState = MutableLiveData<ScreenState>()
    val screenState: LiveData<ScreenState>
        get() = _screenState
    private val screenStateStack = Stack<ScreenState>()

    private val getTrackListUseCase = GetTrackListUseCase(repository)
    private var mediaPlayer: MediaPlayer? = null


    private val coroutineScope = CoroutineScope(Dispatchers.Main)
    private var jobTimer: Job? = null
    private var currentTrackPosition = 0
    private var currentTrackListLength = 0

    init {
        //loading tracks from repository
        loadTrackList()
        //trackList item listener
        adapter.onTrackItemClickListener = { position ->
            val track = trackList.value!![position]
            currentTrackPosition = position
            stopPlayer()
            mediaPlayer = setupPlayer(track)
            startPlayer()
        }
    }

    fun loadTrackList() {
        _trackList.value = getTrackListUseCase.invoke()
        currentTrackListLength = _trackList.value!!.size
    }

    fun navigateTo(screen: ScreenState) {
        if (_screenState.value != screen) {
            screenStateStack.push(screen)
            _screenState.value = screen
        }
    }

    fun goBack(): Boolean {
        if (screenStateStack.size > 1) {
            screenStateStack.pop()
            _screenState.value = screenStateStack.peek()
            return true
        }
        return false
    }

    /*
        PLAYER
     */
    private fun setupPlayer(track: Track): MediaPlayer? {
        val mp = MediaPlayer()

        try {
            _currentTrack.value = track
            mp.setDataSource(application.applicationContext, track.uri)
            mp.prepare()
            return mp
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    private fun startPlayer() {
        if (mediaPlayer == null) return
        if (isPlaying.value == false) {
            mediaPlayer!!.start()
            startTimer()
            _isPlaying.value = true
        }
    }

    private fun startTimer() {
        jobTimer = coroutineScope.launch {
            val duration = mediaPlayer!!.duration / 1000
            while (_currentTimeInSeconds.value!! <= duration) {
                delay(1000)
                _currentTimeInSeconds.value = _currentTimeInSeconds.value!! + 1
            }
            playNext()
        }
    }

    private fun resetTimer() {
        _currentTimeInSeconds.value = 0
    }

    fun seekTo(timeInSeconds: Int) {
        if (mediaPlayer == null) return
        mediaPlayer!!.seekTo(timeInSeconds * 1000)
        _currentTimeInSeconds.value = timeInSeconds
        if (isPlaying.value == false) {
            startPlayer()
        }
    }

    private fun stopTimer() {
        jobTimer?.cancel()
        jobTimer = null
    }

    fun pausePlayer() {
        if (mediaPlayer == null) return
        if (isPlaying.value == false) {
            _isPlaying.value = true
            mediaPlayer!!.start()
            startTimer()
        } else {
            _isPlaying.value = false
            mediaPlayer!!.pause()
            stopTimer()
        }
    }

    private fun stopPlayer() {
        if (mediaPlayer == null) return
        mediaPlayer!!.stop()
        mediaPlayer!!.release()
        mediaPlayer = null
        _isPlaying.value = false
        stopTimer()
        resetTimer()
    }

    fun playNext() {
        stopPlayer()
        currentTrackPosition++
        if (currentTrackPosition == currentTrackListLength) {
            currentTrackPosition = 0
        }
        val nextTrack = trackList.value!![currentTrackPosition]
        mediaPlayer = setupPlayer(nextTrack)
        startPlayer()
    }

    fun playPrev() {
        stopPlayer()
        currentTrackPosition--
        if (currentTrackPosition == -1) {
            currentTrackPosition = currentTrackListLength - 1
        }
        val prevTrack = trackList.value!![currentTrackPosition]
        mediaPlayer = setupPlayer(prevTrack)
        startPlayer()
    }



}