package com.ordresot.playlistmaker.data

import com.ordresot.playlistmaker.data.dto.Preference

interface PreferenceClient {
    fun getData(dto: Preference): Any?
    fun saveData(dto: Preference)
}
