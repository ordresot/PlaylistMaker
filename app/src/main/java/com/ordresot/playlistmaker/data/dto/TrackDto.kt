package com.ordresot.playlistmaker.data.dto

import com.google.gson.annotations.SerializedName
import com.ordresot.playlistmaker.domain.models.Track
import java.text.SimpleDateFormat
import java.util.Locale

class TrackDto(val trackId: Long,
               val trackName: String,
               val artistName: String,
               val collectionName: String?,
               val releaseDate: String,
               val primaryGenreName: String,
               val country: String,
               @SerializedName("trackTimeMillis") val trackTime: Long,
               @SerializedName("artworkUrl100") val artWorkUrl100: String?,
               val previewUrl: String
) {
    fun getTrackTime(): String = SimpleDateFormat("mm:ss", Locale.getDefault()).format(trackTime)
    fun getCoverArtwork() = artWorkUrl100?.replaceAfterLast('/',"512x512bb.jpg")
}