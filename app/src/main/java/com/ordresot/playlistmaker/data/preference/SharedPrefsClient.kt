package com.ordresot.playlistmaker.data.preference

import android.content.Context
import com.google.gson.Gson
import com.ordresot.playlistmaker.data.PreferenceClient
import com.ordresot.playlistmaker.data.dto.Preference

const val PREFERENCES = "preferences"

class SharedPrefsClient(context: Context): PreferenceClient {
    private val sharedPreferences = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE)
    private val gson = Gson()

    override fun getData(dto: Preference): Any? {
        return Gson().fromJson(
            sharedPreferences.getString(dto.key, null),
            dto.type
        )
    }

    override fun saveData(dto: Preference) {
        sharedPreferences.edit().putString(
            dto.key,
            Gson().toJson(dto.value)
        ).apply()
    }
}