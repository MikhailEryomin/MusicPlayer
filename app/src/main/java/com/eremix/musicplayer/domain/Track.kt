package com.eremix.musicplayer.domain

import android.graphics.Bitmap

data class Track (
    val id: Int,
    val title: String,
    val artist: String,
    val thumbNailBitMap: Bitmap?,
    val durationString: String
)