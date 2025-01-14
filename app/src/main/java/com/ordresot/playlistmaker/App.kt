package com.ordresot.playlistmaker

import android.app.Application
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate

const val THEME_PREFERENCES = "theme_preferences"
const val DARK_THEME = "dark_theme"
const val TRACK_EXTRA = "track_extra"

class App: Application() {
    private val themePreferences: SharedPreferences by lazy { getSharedPreferences(THEME_PREFERENCES, MODE_PRIVATE) }
    private var darkTheme = false

    override fun onCreate() {
        super.onCreate()
        switchTheme(themePreferences.getBoolean(DARK_THEME, false))
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
        themePreferences.edit().putBoolean(DARK_THEME, darkTheme).apply()
    }
}