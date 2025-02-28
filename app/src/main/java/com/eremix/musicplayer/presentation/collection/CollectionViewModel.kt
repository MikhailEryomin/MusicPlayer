package com.eremix.musicplayer.presentation.collection

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eremix.musicplayer.data.collection.PlaylistRepositoryImpl
import com.eremix.musicplayer.domain.collection.AddPlaylistUseCase
import com.eremix.musicplayer.domain.collection.DeletePlaylistUseCase
import com.eremix.musicplayer.domain.collection.GetPlaylistsUseCase
import com.eremix.musicplayer.domain.collection.Playlist
import kotlinx.coroutines.launch

class CollectionViewModel: ViewModel(), NewPlaylistDialogFragment.MyInterface {

    private val repository = PlaylistRepositoryImpl
    private val addPlaylistUseCase = AddPlaylistUseCase(repository)
    private val getPlaylistsUseCase = GetPlaylistsUseCase(repository)
    private val removePlaylistUseCase = DeletePlaylistUseCase(repository)

    val playlistCollection = getPlaylistsUseCase()

    //adapter

    override fun newPlaylistSaveData(playlistName: String) {
        Log.d("TAG", "Created new playlist: $playlistName")
        viewModelScope.launch {
            addPlaylistUseCase(playlistName)
        }
    }

    fun removePlaylist(playlistId: Int) {
        viewModelScope.launch {
            removePlaylistUseCase(playlistId)
        }
    }

}