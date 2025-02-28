package com.eremix.musicplayer.data.main

class TrackMapper {

    fun durationInSecondsToString(timeInSeconds: Int): String {
        val minutes = timeInSeconds / 60
        val seconds = timeInSeconds % 60
        return String.format("%02d:%02d", minutes, seconds)
    }

}