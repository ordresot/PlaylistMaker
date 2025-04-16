package com.ordresot.playlistmaker.domain.api.interactor

import com.ordresot.playlistmaker.domain.models.Track

interface HistoryInteractor {
    fun getHistory(consumer: HistoryConsumer)
    fun addToHistory(value: Track, consumer: HistoryConsumer)
    fun clearHistory()

    interface HistoryConsumer{
        fun consume(value: List<Track>)
    }
}