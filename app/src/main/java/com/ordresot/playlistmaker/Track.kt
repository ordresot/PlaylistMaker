package com.ordresot.playlistmaker

import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat
import java.util.Locale

data class Track (private val trackId: Long,
                  private val trackName: String,
                  private val artistName: String,
                  private val collectionName: String?,
                  private val releaseDate: String,
                  private val primaryGenreName: String,
                  private val country: String,
                  @SerializedName("trackTimeMillis") private val trackTime: Long,
                  @SerializedName("artworkUrl100") private val artWorkUrl100: String?) {

    fun getTrackName() = trackName
    fun getTrackTime(): String = SimpleDateFormat("mm:ss", Locale.getDefault()).format(trackTime)
    fun getTrackArtist() = artistName
    fun getTrackImage() = artWorkUrl100
    fun getTrackId() = trackId
    fun getCoverArtwork() = artWorkUrl100?.replaceAfterLast('/',"512x512bb.jpg")
    fun getTrackCollectionName() = collectionName
    fun getTrackReleaseDate() = releaseDate
    fun getTrackGenreName() = primaryGenreName
    fun getTrackCountry() = country
}