package com.ordresot.playlistmaker.domain.impl.interactor

import com.ordresot.playlistmaker.domain.api.interactor.PreferencesInteractor
import com.ordresot.playlistmaker.domain.api.repository.PreferencesRepository

class PreferencesInteractorImpl(private val repository: PreferencesRepository): PreferencesInteractor {

    override fun getDarkTheme(consumer: PreferencesInteractor.PreferencesConsumer) {
        consumer.consume(repository.getDarkTheme())
    }

    override fun setDarkTheme(value: Boolean) {
        repository.saveDarkTheme(value)
    }

    override fun getFirstRun(consumer: PreferencesInteractor.PreferencesConsumer) {
        consumer.consume(repository.getFirstRun())
    }

    override fun setFirstRun(value: Boolean) {
        repository.saveFirstRun(value)
    }
}