package com.eremix.musicplayer.data.collection

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.eremix.musicplayer.data.PlaylistDatabase
import com.eremix.musicplayer.domain.collection.Playlist
import com.eremix.musicplayer.domain.collection.PlaylistRepository

object PlaylistRepositoryImpl: PlaylistRepository {

    private lateinit var application: Application
    private lateinit var playlistDatabase: PlaylistDatabase
    private lateinit var collectionDao: CollectionDao

    fun init(application: Application) {
        this.application = application
        playlistDatabase = PlaylistDatabase.getInstance(application)
        collectionDao = playlistDatabase.getCollectionDao()
    }


    private val mapper = CollectionMapper()

    override fun getPlaylistCollection(): LiveData<List<Playlist>> {
        val dbModelLiveData = collectionDao.getPlaylistCollection()
        val entityLiveData = dbModelLiveData.map {
            mapper.dbModelListToEntityList(it)
        }
        return entityLiveData
    }

    override suspend fun getPlaylist(playlistId: Int): Playlist {
        val dbModel = collectionDao.getPlaylist(playlistId)
        return mapper.dbModelToEntity(dbModel)
    }

    override suspend fun addNewPlaylist(playlistName: String) {
        val emptyPlaylist = Playlist(
            id = 0,
            name = playlistName,
            trackIdList = listOf()
        )
        collectionDao.insertNewPlaylist(mapper.entityToDbModel(emptyPlaylist))
    }

    override suspend fun deletePlaylist(playlistId: Int) {
        collectionDao.deletePlaylist(playlistId)
    }


}