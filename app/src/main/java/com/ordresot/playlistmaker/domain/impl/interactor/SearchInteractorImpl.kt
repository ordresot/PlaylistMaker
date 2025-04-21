package com.ordresot.playlistmaker.domain.impl.interactor

import com.ordresot.playlistmaker.domain.api.interactor.SearchInteractor
import com.ordresot.playlistmaker.domain.api.repository.TracksRepository
import com.ordresot.playlistmaker.util.Resource
import java.util.concurrent.Executors


class SearchInteractorImpl(private val repository: TracksRepository): SearchInteractor {
    private val executor = Executors.newCachedThreadPool()

    override fun searchTracks(expression: String, consumer: SearchInteractor.SearchConsumer) {
        executor.execute{
            when (val searched = repository.searchTracks(expression)) {
                is Resource.Success -> { consumer.consume(searched.data, null) }
                is Resource.Error -> { consumer.consume(null, searched.message) }
            }
        }
    }

}