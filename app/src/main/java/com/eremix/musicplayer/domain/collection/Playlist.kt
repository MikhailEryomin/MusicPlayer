package com.eremix.musicplayer.domain.collection

import com.eremix.musicplayer.domain.main.Track

data class Playlist(
    val id: Int,
    val name: String,
    val trackIdList: List<Int>
)