package com.ordresot.playlistmaker.data.repository

import com.ordresot.playlistmaker.data.PreferenceClient
import com.ordresot.playlistmaker.data.dto.DarkThemePreference
import com.ordresot.playlistmaker.data.dto.FirstRunPreference
import com.ordresot.playlistmaker.data.dto.HistoryPreference
import com.ordresot.playlistmaker.data.dto.TrackDto
import com.ordresot.playlistmaker.domain.api.repository.PreferencesRepository
import com.ordresot.playlistmaker.domain.models.Track
import java.text.SimpleDateFormat
import java.util.Locale

class PreferencesRepositoryImpl(private val preferenceClient: PreferenceClient):
    PreferencesRepository {
    override fun getSearchHistory(): List<Track> {
        val value = preferenceClient.getData(HistoryPreference()) as? List<TrackDto> ?: emptyList()
        return value.map {
            Track(
                trackId = it.trackId,
                trackName = it.trackName,
                artistName = it.artistName,
                collectionName = it.collectionName,
                releaseDate = it.releaseDate,
                primaryGenreName = it.primaryGenreName,
                country = it.country,
                trackTime = it.getTrackTime(),
                artWorkUrl100 = it.artWorkUrl100,
                coverArtwork = it.getCoverArtwork(),
                previewUrl = it.previewUrl
            )
        }
    }

    override fun saveSearchHistory(value: List<Track>) {
        val data = value.map {
            TrackDto(
                trackId = it.trackId,
                trackName = it.trackName,
                artistName = it.artistName,
                collectionName = it.collectionName,
                releaseDate = it.releaseDate,
                primaryGenreName = it.primaryGenreName,
                country = it.country,
                trackTime = SimpleDateFormat("mm:ss", Locale.getDefault()).parse(it.trackTime)?.time ?: 0,
                artWorkUrl100 = it.artWorkUrl100,
                previewUrl = it.previewUrl
            )
        }
        preferenceClient.saveData(HistoryPreference(data))
    }

    override fun getDarkTheme(): Boolean {
        val value = preferenceClient.getData(DarkThemePreference()) as? Boolean
        return value ?: false
    }

    override fun saveDarkTheme(value: Boolean) {
        preferenceClient.saveData(DarkThemePreference(value))
    }

    override fun getFirstRun(): Boolean {
        val value = preferenceClient.getData(FirstRunPreference()) as? Boolean
        return value ?: true
    }

    override fun saveFirstRun(value: Boolean) {
        preferenceClient.saveData(FirstRunPreference(value))
    }

}