package com.ordresot.playlistmaker.data.repository

import com.ordresot.playlistmaker.data.NetworkClient
import com.ordresot.playlistmaker.data.dto.TrackSearchRequest
import com.ordresot.playlistmaker.data.dto.TrackSearchResponse
import com.ordresot.playlistmaker.domain.api.repository.TracksRepository
import com.ordresot.playlistmaker.domain.models.Track
import com.ordresot.playlistmaker.util.Resource

class TracksRepositoryImpl(private val networkClient: NetworkClient): TracksRepository {
    override fun searchTracks(expression: String): Resource<List<Track>> {
        val response = networkClient.makeRequest(TrackSearchRequest(expression))
        return when (response.resultCode) {
            200 -> {
                Resource.Success(
                    (response as TrackSearchResponse).results.map {
                        Track(it.trackId, it.trackName, it.artistName, it.collectionName, it.releaseDate, it.primaryGenreName, it.country, it.getTrackTime(), it.artWorkUrl100, it.getCoverArtwork(), it.previewUrl)
                    }
                )
            }
            -1 -> {
                Resource.Error("Проблемы со связью\n\nЗагрузка не удалась. Проверьте подключение к интернету")
            }
            else -> {
                Resource.Error("Ошибка сервера")
            }
        }
    }
}