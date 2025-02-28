package com.eremix.musicplayer.domain.collection

import androidx.lifecycle.LiveData

class GetPlaylistsUseCase(private val repository: PlaylistRepository) {

    operator fun invoke(): LiveData<List<Playlist>> {
        return repository.getPlaylistCollection()
    }

}