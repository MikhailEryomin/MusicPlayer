package com.eremix.musicplayer.domain.main

interface TrackListRepository {

    fun getTrackList(): List<Track>

    fun getTrackItem(trackId: Int): Track

    fun addTrackItem(trackItem: Track)

}