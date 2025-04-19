package com.ordresot.playlistmaker.domain.models

data class Track (val trackId: Long,
                  val trackName: String,
                  val artistName: String,
                  val collectionName: String?,
                  val releaseDate: String,
                  val primaryGenreName: String,
                  val country: String,
                  val trackTime: String,
                  val artWorkUrl100: String?,
                  val coverArtwork: String?,
                  val previewUrl: String
)