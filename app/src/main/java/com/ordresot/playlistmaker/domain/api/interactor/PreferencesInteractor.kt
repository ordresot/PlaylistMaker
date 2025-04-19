package com.ordresot.playlistmaker.domain.api.interactor

interface PreferencesInteractor {
    fun getDarkTheme(consumer: PreferencesConsumer)
    fun setDarkTheme(value: Boolean)

    fun getFirstRun(consumer: PreferencesConsumer)
    fun setFirstRun(value: Boolean)

    interface PreferencesConsumer{
       fun consume(value: Boolean)
    }
}