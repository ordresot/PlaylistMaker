package com.ordresot.playlistmaker

data class Track (private val trackName: String,
                  private val artistName: String,
                  private val trackTime: String,
                  private val artWorkUrl100: String) {

    fun getTrackName() = trackName
    fun getTrackTime() = trackTime
    fun getTrackArtist() = artistName
    fun getTrackImage() = artWorkUrl100
}