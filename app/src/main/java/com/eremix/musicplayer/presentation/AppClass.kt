package com.eremix.musicplayer.presentation

import android.app.Application
import com.eremix.musicplayer.data.TrackListRepositoryImpl

class AppClass: Application() {

    override fun onCreate() {
        super.onCreate()
        TrackListRepositoryImpl.init(this)
    }

}