package com.ordresot.playlistmaker.data.preference

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.ordresot.playlistmaker.data.PreferenceClient
import com.ordresot.playlistmaker.data.dto.Preference

const val PREFERENCES = "preferences"

class SharedPrefsClient(private val sharedPreferences: SharedPreferences): PreferenceClient {
    private val gson = Gson()

    override fun getData(dto: Preference): Any? {
        return gson.fromJson(
            sharedPreferences.getString(dto.key, null),
            dto.type
        )
    }

    override fun saveData(dto: Preference) {
        sharedPreferences.edit().putString(
            dto.key,
            gson.toJson(dto.value)
        ).apply()
    }
}