package com.eremix.musicplayer.domain.collection

class AddPlaylistUseCase(private val repository: PlaylistRepository) {

    suspend operator fun invoke(playlistName: String) {
        repository.addNewPlaylist(playlistName)
    }

}