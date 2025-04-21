package com.ordresot.playlistmaker.domain.api.repository

import com.ordresot.playlistmaker.domain.models.Track
import com.ordresot.playlistmaker.util.Resource

interface TracksRepository {
    fun searchTracks(expression: String): Resource<List<Track>>
}