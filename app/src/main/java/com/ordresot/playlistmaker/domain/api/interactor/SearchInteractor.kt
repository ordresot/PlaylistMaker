package com.ordresot.playlistmaker.domain.api.interactor

import com.ordresot.playlistmaker.domain.models.Track

interface SearchInteractor {
    fun searchTracks(expression: String, consumer: SearchConsumer)

    interface SearchConsumer{
        fun consume(foundTracks: List<Track>?, errorMessage: String?)
    }
}