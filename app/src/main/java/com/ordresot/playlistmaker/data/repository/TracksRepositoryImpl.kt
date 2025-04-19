package com.ordresot.playlistmaker.data.repository

import com.ordresot.playlistmaker.data.NetworkClient
import com.ordresot.playlistmaker.data.dto.TrackSearchRequest
import com.ordresot.playlistmaker.data.dto.TrackSearchResponse
import com.ordresot.playlistmaker.domain.api.repository.TracksRepository
import com.ordresot.playlistmaker.domain.models.Track

class TracksRepositoryImpl(private val networkClient: NetworkClient): TracksRepository {
    override fun searchTracks(expression: String): List<Track>? {
        val response = networkClient.makeRequest(TrackSearchRequest(expression))
        return if (response.resultCode == 200) {
            (response as TrackSearchResponse).results.map {
                Track(it.trackId, it.trackName, it.artistName, it.collectionName, it.releaseDate, it.primaryGenreName, it.country, it.getTrackTime(), it.artWorkUrl100, it.getCoverArtwork(), it.previewUrl)
            }
        } else if (response.resultCode == 400){
            null
        } else {
            emptyList()
        }
    }
}