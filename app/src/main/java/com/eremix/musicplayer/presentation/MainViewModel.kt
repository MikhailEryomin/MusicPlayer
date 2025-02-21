package com.eremix.musicplayer.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.eremix.musicplayer.data.TrackListRepositoryImpl
import com.eremix.musicplayer.domain.GetTrackListUseCase
import com.eremix.musicplayer.domain.Track
import java.util.Stack


class MainViewModel(application: Application): AndroidViewModel(application) {

    //will be fixed with dagger soon
    private val repository = TrackListRepositoryImpl

    val adapter = TrackListAdapter(application)

    private val _trackList = MutableLiveData<List<Track>>()
    val trackList: LiveData<List<Track>>
        get() = _trackList

    private val _currentTrack = MutableLiveData<Track>()
    val currentTrack: LiveData<Track>
        get() = _currentTrack

    private val _screenState = MutableLiveData<ScreenState>()
    val screenState: LiveData<ScreenState>
        get() = _screenState
    private val screenStateStack = Stack<ScreenState>()

    private val getTrackListUseCase = GetTrackListUseCase(repository)

    init {
        //loading tracks from repository
        loadTrackList()
        adapter.onTrackItemClickListener = { trackItem ->
            _currentTrack.value = trackItem
        }
    }

    private fun loadTrackList() {
        _trackList.value = getTrackListUseCase.invoke()
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




}