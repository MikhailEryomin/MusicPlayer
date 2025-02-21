package com.eremix.musicplayer.domain

interface TrackListRepository {

    fun getTrackList(): List<Track>

    fun getTrackItem(trackId: Int): Track

    fun addTrackItem(trackItem: Track)

}