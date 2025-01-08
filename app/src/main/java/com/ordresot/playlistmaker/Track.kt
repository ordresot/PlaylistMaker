package com.ordresot.playlistmaker

import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat
import java.util.Locale

data class Track (private val trackId: Long,
                  private val trackName: String,
                  private val artistName: String,
                  @SerializedName("trackTimeMillis") private val trackTime: Long,
                  @SerializedName("artworkUrl100") private val artWorkUrl100: String) {

    fun getTrackName() = trackName
    fun getTrackTime(): String = SimpleDateFormat("mm:ss", Locale.getDefault()).format(trackTime)
    fun getTrackArtist() = artistName
    fun getTrackImage() = artWorkUrl100
    fun getTrackId() = trackId
}