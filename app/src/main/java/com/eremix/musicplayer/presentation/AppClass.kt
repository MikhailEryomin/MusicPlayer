package com.eremix.musicplayer.presentation

import android.app.Application
import com.eremix.musicplayer.data.collection.PlaylistRepositoryImpl
import com.eremix.musicplayer.data.main.TrackListRepositoryImpl

class AppClass: Application() {

    override fun onCreate() {
        super.onCreate()
        TrackListRepositoryImpl.init(this)
        PlaylistRepositoryImpl.init(this)
    }

}