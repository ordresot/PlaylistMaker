package com.ordresot.playlistmaker.domain.api.repository

import com.ordresot.playlistmaker.domain.models.Track

interface TracksRepository {
    fun searchTracks(expression: String): List<Track>?
}