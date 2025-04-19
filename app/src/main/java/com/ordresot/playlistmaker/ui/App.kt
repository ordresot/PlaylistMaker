package com.ordresot.playlistmaker.ui

import android.app.Application
import android.app.UiModeManager
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import com.ordresot.playlistmaker.Creator
import com.ordresot.playlistmaker.domain.api.interactor.PreferencesInteractor

const val TRACK_EXTRA = "track_extra"

class App: Application() {
    override fun onCreate() {
        super.onCreate()
        Creator.initContext(this)
    }
}