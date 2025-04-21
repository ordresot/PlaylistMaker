package com.ordresot.playlistmaker.ui

import android.app.Application
import com.ordresot.playlistmaker.util.Creator

const val TRACK_EXTRA = "track_extra"

class App: Application() {
    override fun onCreate() {
        super.onCreate()
        Creator.initContext(this)
    }
}