package com.ordresot.playlistmaker.data.dto

import com.google.gson.reflect.TypeToken

class HistoryPreference(): Preference(key = PreferenceKey.SEARCH_HISTORY.key, type = object : TypeToken<ArrayList<TrackDto>>() {}.type){
    constructor(value: List<TrackDto>) : this() {
        this.value = value
    }
}
