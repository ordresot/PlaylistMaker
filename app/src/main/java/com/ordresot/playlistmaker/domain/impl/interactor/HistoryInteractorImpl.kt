package com.ordresot.playlistmaker.domain.impl.interactor

import com.ordresot.playlistmaker.domain.api.interactor.HistoryInteractor
import com.ordresot.playlistmaker.domain.api.repository.PreferencesRepository
import com.ordresot.playlistmaker.domain.models.Track

class HistoryInteractorImpl(private val repository: PreferencesRepository): HistoryInteractor {

    override fun getHistory(consumer: HistoryInteractor.HistoryConsumer) {
        consumer.consume(repository.getSearchHistory())
    }

    override fun addToHistory(value: Track, consumer: HistoryInteractor.HistoryConsumer) {
        val trackList = repository.getSearchHistory().toMutableList()
        if (trackList.size > 0){
            val existingIndex = trackList.indexOfFirst { it.trackId == value.trackId }
            if (existingIndex != -1){
                trackList.removeAt(existingIndex)
            }
            if (trackList.size == 10){
                trackList.removeAt(9)
            }
        }
        trackList.add(0, value)
        repository.saveSearchHistory(trackList)
        consumer.consume(trackList)
    }

    override fun clearHistory() {
        repository.saveSearchHistory(emptyList())
    }


}