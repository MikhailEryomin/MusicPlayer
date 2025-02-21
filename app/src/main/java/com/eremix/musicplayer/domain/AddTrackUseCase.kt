package com.eremix.musicplayer.domain

class AddTrackUseCase(private val repository: TrackListRepository) {

    operator fun invoke(trackItem: Track) {
        repository.addTrackItem(trackItem)
    }
}