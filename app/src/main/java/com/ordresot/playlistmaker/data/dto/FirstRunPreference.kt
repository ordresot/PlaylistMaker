package com.ordresot.playlistmaker.data.dto

import com.google.gson.reflect.TypeToken

class FirstRunPreference(): Preference(key = PreferenceKey.IS_FIRST_RUN.key, type = object : TypeToken<Boolean>() {}.type) {
    constructor(value: Boolean) : this() {
        this.value = value
    }
}