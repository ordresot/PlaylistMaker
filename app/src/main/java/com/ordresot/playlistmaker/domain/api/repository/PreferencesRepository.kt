package com.ordresot.playlistmaker.domain.api.repository

import com.ordresot.playlistmaker.domain.models.Track

interface PreferencesRepository {
    fun getSearchHistory(): List<Track>
    fun saveSearchHistory(value: List<Track>)

    fun getDarkTheme(): Boolean
    fun saveDarkTheme(value: Boolean)

    fun getFirstRun(): Boolean
    fun saveFirstRun(value: Boolean)
}