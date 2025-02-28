package com.eremix.musicplayer.data.collection

import com.eremix.musicplayer.domain.collection.Playlist

class CollectionMapper {

    fun dbModelToEntity(dbModel: PlaylistDbModel): Playlist {
        return Playlist(
            id = dbModel.id,
            name = dbModel.name,
            trackIdList = dbModel.trackIdList
        )
    }

    fun dbModelListToEntityList(dbModelList: List<PlaylistDbModel>): List<Playlist> {
        return dbModelList.map { dbModelToEntity(it) }
    }

    fun entityToDbModel(entity: Playlist): PlaylistDbModel {
        return PlaylistDbModel(
            id = 0,
            name = entity.name,
            trackIdList = entity.trackIdList
        )
    }

}