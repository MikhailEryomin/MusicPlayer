package com.eremix.musicplayer.data.collection

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CollectionDao {

    //suspend - to call dao methods in coroutines (Dispatchers.IO)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNewPlaylist(playlist: PlaylistDbModel)

    @Query("DELETE FROM playlists WHERE id=:playlistId")
    suspend fun deletePlaylist(playlistId: Int)

    @Query("SELECT * FROM playlists WHERE id=:playlistId LIMIT 1")
    suspend fun getPlaylist(playlistId: Int): PlaylistDbModel

    @Query("SELECT * FROM playlists")
    fun getPlaylistCollection(): LiveData<List<PlaylistDbModel>>

}