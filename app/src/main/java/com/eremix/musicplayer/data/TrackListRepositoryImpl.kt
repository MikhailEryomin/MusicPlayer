package com.eremix.musicplayer.data

import com.eremix.musicplayer.domain.Track
import com.eremix.musicplayer.domain.TrackListRepository
import kotlin.random.Random

object TrackListRepositoryImpl: TrackListRepository {

    //trackListDao
    private val testTrackList = mutableListOf<Track>()
    private var autoIncrementId = 0

    override fun getTrackList(): List<Track> {
        repeat(15) {
            val testTrack = Track(
                autoIncrementId++,
                "Title ${Random.nextInt(0, 100)}",
                "Artist ${Random.nextInt(50, 100)}",
                null,
                "${Random.nextInt(0, 10)}:${Random.nextInt(10, 60)}"
            )
            testTrackList.add(testTrack)
        }
        return testTrackList.toList()
    }

    override fun getTrackItem(trackId: Int): Track {
        return testTrackList[trackId]
    }

    override fun addTrackItem(trackItem: Track) {
        //Some logic
    }
}