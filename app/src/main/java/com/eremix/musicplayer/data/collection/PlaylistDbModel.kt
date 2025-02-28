package com.eremix.musicplayer.data.collection

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.eremix.musicplayer.domain.main.Track

@Entity(tableName = "playlists")
data class PlaylistDbModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String,
    val trackIdList: List<Int>
)