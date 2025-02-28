package com.eremix.musicplayer.domain.main

class GetTrackUseCase(private val repository: TrackListRepository) {

    operator fun invoke(id: Int): Track {
        return repository.getTrackItem(id)
    }

}