package com.ordresot.playlistmaker

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

const val SEARCH_HISTORY = "search_history"
const val TRACK_LIST = "track_list"

class History(private val searchHistory: SharedPreferences) {
    private var trackList = ArrayList<Track>()

    fun historyLoad(): ArrayList<Track> {
        val json = searchHistory.getString(TRACK_LIST, null)
        if (!json.isNullOrEmpty()){
            trackList = Gson().fromJson(json, object : TypeToken<ArrayList<Track>>() {}.type)
        }
        return trackList
    }

    private fun historySave() {
        val json = Gson().toJson(trackList)
        searchHistory.edit()
            .putString(TRACK_LIST, json)
            .apply()
    }

    fun addTrack(track: Track){
        trackList = historyLoad()
        if (trackList.size > 0){
            val existingIndex = trackList.indexOfFirst { it.getTrackId() == track.getTrackId() }
            if (existingIndex != -1){
                trackList.removeAt(existingIndex)
            }
            if (trackList.size == 10){
                trackList.removeAt(9)
            }
        }
        trackList.add(0, track)
        historySave()
    }

    fun clear(){
        trackList.clear()
        searchHistory.edit().remove(TRACK_LIST).apply()
    }

    fun isEmpty(): Boolean{
        return trackList.isEmpty()
    }
}