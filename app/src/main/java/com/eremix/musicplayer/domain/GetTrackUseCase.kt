package com.eremix.musicplayer.domain

class GetTrackUseCase(private val repository: TrackListRepository) {

    operator fun invoke(id: Int): Track {
        return repository.getTrackItem(id)
    }

}