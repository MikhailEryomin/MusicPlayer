package com.eremix.musicplayer.domain

class GetTrackListUseCase(private val repository: TrackListRepository) {

    operator fun invoke(): List<Track> {
        return repository.getTrackList()
    }
}