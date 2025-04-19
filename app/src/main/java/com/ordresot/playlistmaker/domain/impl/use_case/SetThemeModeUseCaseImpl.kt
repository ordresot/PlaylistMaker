package com.ordresot.playlistmaker.domain.impl.use_case

import androidx.appcompat.app.AppCompatDelegate
import com.ordresot.playlistmaker.domain.api.use_case.SetThemeModeUseCase

class SetThemeModeUseCaseImpl: SetThemeModeUseCase {
    override fun setThemeMode(darkThemeEnabled: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}