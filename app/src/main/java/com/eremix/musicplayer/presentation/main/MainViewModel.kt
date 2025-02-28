package com.eremix.musicplayer.presentation.main

import android.app.Application
import android.media.MediaPlayer
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.eremix.musicplayer.data.main.TrackListRepositoryImpl
import com.eremix.musicplayer.data.main.TrackMapper
import com.eremix.musicplayer.domain.main.GetTrackListUseCase
import com.eremix.musicplayer.domain.main.Track
import com.eremix.musicplayer.presentation.ScreenState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Stack


class MainViewModel(private val application: Application): AndroidViewModel(application) {

    //will be fixed with dagger soon
    private val repository = TrackListRepositoryImpl

    val adapter = TrackListAdapter(application)

    private val _trackListLD = MutableLiveData<List<Track>>()
    val trackListLD: LiveData<List<Track>>
        get() = _trackListLD
    private val trackList: List<Track>
        get() = trackListLD.value ?: listOf()

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

    private val _listIsNotEmpty = MutableLiveData<Boolean>()
    val listIsNotEmpty: LiveData<Boolean>
        get() = _listIsNotEmpty

    private val getTrackListUseCase = GetTrackListUseCase(repository)
    private var mediaPlayer: MediaPlayer? = null


    private val coroutineScope = CoroutineScope(Dispatchers.Main)
    private var jobTimer: Job? = null
    private var currentTrackPosition = 0
    private var currentTrackListLength = 0

    init {
        setupTrackList()
        setupTrackListClickListener()
    }

    private fun setupTrackListClickListener() {
        adapter.onTrackItemClickListener = { position ->
            val track = trackListLD.value!![position]
            currentTrackPosition = position
            stopPlayer()
            mediaPlayer = setupPlayer(track)
            startPlayer()
        }
    }

    private fun initPlayer() {
        if (trackList.isNotEmpty()) {
            _listIsNotEmpty.value = true
            val firstTrack = trackList[currentTrackPosition]
            mediaPlayer = setupPlayer(firstTrack)
        } else {
            //there are not tracks in the list
            _listIsNotEmpty.value = false
        }
    }

    fun setupTrackList() {
        _trackListLD.value = getTrackListUseCase.invoke()
        currentTrackListLength = _trackListLD.value!!.size
        initPlayer()
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

    /* PLAYER
     */
    private fun setupPlayer(track: Track): MediaPlayer? {
        val mp = MediaPlayer()
        try {
            _currentTrack.value = track
            adapter.setPosition(currentTrackPosition)
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
            mediaPlayer?.start()
            _isPlaying.value = true

            startTimer()
        }
    }

    fun pausePlayer() {
        if (mediaPlayer == null) return
        if (isPlaying.value == false) {
            startPlayer()
        } else {
            _isPlaying.value = false
            mediaPlayer?.pause()
            stopTimer()
        }
    }

    private fun stopPlayer() {
        if (mediaPlayer == null) return
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
        _isPlaying.value = false
        stopTimer()
        resetTimer()
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

    private fun stopTimer() {
        jobTimer?.cancel()
        jobTimer = null
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

    fun playNext() {
        stopPlayer()
        currentTrackPosition++
        if (currentTrackPosition == currentTrackListLength) {
            currentTrackPosition = 0
        }
        val nextTrack = trackListLD.value!![currentTrackPosition]
        mediaPlayer = setupPlayer(nextTrack)
        startPlayer()
    }

    fun playPrev() {
        stopPlayer()
        currentTrackPosition--
        if (currentTrackPosition == -1) {
            currentTrackPosition = currentTrackListLength - 1
        }
        val prevTrack = trackListLD.value!![currentTrackPosition]
        mediaPlayer = setupPlayer(prevTrack)
        startPlayer()
    }



}