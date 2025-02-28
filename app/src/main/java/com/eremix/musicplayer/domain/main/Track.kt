package com.eremix.musicplayer.domain.main

import android.graphics.Bitmap
import android.net.Uri

data class Track (
    val uri: Uri,
    val id: Int,
    val title: String,
    val artist: String,
    val thumbnailBitmap: Bitmap?,
    val durationInSeconds: Int,
    val durationString: String
)