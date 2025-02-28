package com.eremix.musicplayer.domain.collection

class DeletePlaylistUseCase(private val repository: PlaylistRepository) {

    suspend operator fun invoke(playlistId: Int) {
        repository.deletePlaylist(playlistId)
    }

}