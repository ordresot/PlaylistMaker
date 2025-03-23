package com.ordresot.playlistmaker

import android.app.Application
import android.app.UiModeManager
import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate

const val THEME_PREFERENCES = "theme_preferences"
const val DARK_THEME = "dark_theme"
const val TRACK_EXTRA = "track_extra"
const val IS_FIRST_RUN = "is_first_run"

class App: Application() {
    private val themePreferences: SharedPreferences by lazy { getSharedPreferences(THEME_PREFERENCES, MODE_PRIVATE) }

    override fun onCreate() {
        super.onCreate()
        var isDarkTheme = themePreferences.getBoolean(DARK_THEME, false)
        if (themePreferences.getBoolean(IS_FIRST_RUN, true)){
            val uiModeManager = getSystemService(Context.UI_MODE_SERVICE) as UiModeManager
            isDarkTheme = uiModeManager.nightMode == UiModeManager.MODE_NIGHT_YES
            themePreferences.edit().putBoolean(IS_FIRST_RUN, false).apply()
        }
        (applicationContext as App).switchTheme(isDarkTheme)
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
        themePreferences.edit().putBoolean(DARK_THEME, darkThemeEnabled).apply()
    }
}