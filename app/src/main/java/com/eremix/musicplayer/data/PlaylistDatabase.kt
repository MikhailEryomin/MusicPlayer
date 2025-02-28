package com.eremix.musicplayer.data

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.eremix.musicplayer.data.collection.CollectionDao
import com.eremix.musicplayer.data.collection.PlaylistDbModel
import com.eremix.musicplayer.data.collection.PlaylistTypeConverter

@Database(version = 2, entities = [PlaylistDbModel::class], exportSchema = false)
@TypeConverters(PlaylistTypeConverter::class)
abstract class PlaylistDatabase: RoomDatabase() {

    abstract fun getCollectionDao(): CollectionDao

    companion object {

        private var INSTANCE: PlaylistDatabase? = null
        private val LOCK = Any()
        private const val DB_NAME = "shop_item.db"

        fun getInstance(application: Application): PlaylistDatabase {
            INSTANCE?.let {
                return it
            }
            synchronized(LOCK) {
                INSTANCE?.let {
                    return it
                }
                val db = Room.databaseBuilder(
                    application,
                    PlaylistDatabase::class.java,
                    DB_NAME
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = db
                return db
            }
        }

    }

}