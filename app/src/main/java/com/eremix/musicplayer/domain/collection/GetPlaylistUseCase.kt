package com.eremix.musicplayer.domain.collection

class GetPlaylistUseCase(private val repository: PlaylistRepository) {

    suspend operator fun invoke(playlistId: Int): Playlist? {
        return repository.getPlaylist(playlistId)
    }

}