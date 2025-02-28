package com.eremix.musicplayer.domain.collection

import androidx.lifecycle.LiveData

interface PlaylistRepository {
    suspend fun addNewPlaylist(playlistName: String)

    suspend fun deletePlaylist(playlistId: Int)

    fun getPlaylistCollection(): LiveData<List<Playlist>>

    suspend fun getPlaylist(playlistId: Int): Playlist?
}