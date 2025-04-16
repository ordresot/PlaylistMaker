package com.ordresot.playlistmaker.data.dto

import com.google.gson.reflect.TypeToken

class DarkThemePreference(): Preference(key = PreferenceKey.DARK_THEME.key, type = object : TypeToken<Boolean>() {}.type) {
    constructor(value: Boolean) : this() {
        this.value = value
    }
}