package com.ordresot.playlistmaker

import android.content.Context
import com.ordresot.playlistmaker.data.repository.PreferencesRepositoryImpl
import com.ordresot.playlistmaker.data.repository.TracksRepositoryImpl
import com.ordresot.playlistmaker.data.network.RetrofitNetworkClient
import com.ordresot.playlistmaker.data.preference.SharedPrefsClient
import com.ordresot.playlistmaker.data.repository.PlayerRepositoryImpl
import com.ordresot.playlistmaker.domain.api.interactor.HistoryInteractor
import com.ordresot.playlistmaker.domain.api.interactor.PlayerInteractor
import com.ordresot.playlistmaker.domain.api.repository.PlayerRepository
import com.ordresot.playlistmaker.domain.api.interactor.PreferencesInteractor
import com.ordresot.playlistmaker.domain.api.repository.PreferencesRepository
import com.ordresot.playlistmaker.domain.api.interactor.SearchInteractor
import com.ordresot.playlistmaker.domain.api.interactor.SettingsInteractor
import com.ordresot.playlistmaker.domain.api.repository.TracksRepository
import com.ordresot.playlistmaker.domain.api.use_case.ClickDebounceUseCase
import com.ordresot.playlistmaker.domain.api.use_case.SetThemeModeUseCase
import com.ordresot.playlistmaker.domain.impl.interactor.HistoryInteractorImpl
import com.ordresot.playlistmaker.domain.impl.interactor.PlayerInteractorImpl
import com.ordresot.playlistmaker.domain.impl.interactor.PreferencesInteractorImpl
import com.ordresot.playlistmaker.domain.impl.interactor.SearchInteractorImpl
import com.ordresot.playlistmaker.domain.impl.interactor.SettingsInteractorImpl
import com.ordresot.playlistmaker.domain.impl.use_case.ClickDebounceUseCaseImpl
import com.ordresot.playlistmaker.domain.impl.use_case.SetThemeModeUseCaseImpl

object Creator {
    private fun getTracksRepository(): TracksRepository {
        return TracksRepositoryImpl(RetrofitNetworkClient())
    }

    fun provideSearchInteractor(): SearchInteractor {
        return SearchInteractorImpl(getTracksRepository())
    }

    private fun getPreferencesRepository(context: Context): PreferencesRepository {
        return PreferencesRepositoryImpl(SharedPrefsClient(context))
    }

    fun providePreferencesInteractor(context: Context): PreferencesInteractor {
        return PreferencesInteractorImpl(getPreferencesRepository(context))
    }

    fun provideHistoryInteractor(context: Context): HistoryInteractor {
        return HistoryInteractorImpl(getPreferencesRepository(context))
    }

    private fun getPlayerRepository(): PlayerRepository {
        return PlayerRepositoryImpl()
    }

    fun providePlayerInteractor(): PlayerInteractor {
        return PlayerInteractorImpl(getPlayerRepository())
    }

    fun provideSetThemeModeUseCase(): SetThemeModeUseCase {
        return SetThemeModeUseCaseImpl()
    }

    fun provideClickDebounceUseCase(): ClickDebounceUseCase {
        return ClickDebounceUseCaseImpl()
    }

    fun provideSettingsInteractor(context: Context): SettingsInteractor {
        return SettingsInteractorImpl(context)
    }
}