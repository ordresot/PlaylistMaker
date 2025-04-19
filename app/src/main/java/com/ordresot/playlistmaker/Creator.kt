package com.ordresot.playlistmaker

import android.content.Context
import android.media.MediaPlayer
import com.ordresot.playlistmaker.data.network.ITunesApiService
import com.ordresot.playlistmaker.data.network.RetrofitNetworkClient
import com.ordresot.playlistmaker.data.preference.PREFERENCES
import com.ordresot.playlistmaker.data.preference.SharedPrefsClient
import com.ordresot.playlistmaker.data.repository.PlayerRepositoryImpl
import com.ordresot.playlistmaker.data.repository.PreferencesRepositoryImpl
import com.ordresot.playlistmaker.data.repository.TracksRepositoryImpl
import com.ordresot.playlistmaker.domain.api.interactor.HistoryInteractor
import com.ordresot.playlistmaker.domain.api.interactor.PlayerInteractor
import com.ordresot.playlistmaker.domain.api.interactor.PreferencesInteractor
import com.ordresot.playlistmaker.domain.api.interactor.SearchInteractor
import com.ordresot.playlistmaker.domain.api.interactor.SettingsInteractor
import com.ordresot.playlistmaker.domain.api.repository.PlayerRepository
import com.ordresot.playlistmaker.domain.api.repository.PreferencesRepository
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
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Creator {
    private lateinit var applicationContext: Context

    fun initContext(context: Context) {
        applicationContext = context.applicationContext
    }

    private fun getTracksRepository(): TracksRepository {
        return TracksRepositoryImpl(
            RetrofitNetworkClient(
                Retrofit.Builder()
                    .baseUrl("https://itunes.apple.com")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(ITunesApiService::class.java)
            )
        )
    }

    fun provideSearchInteractor(): SearchInteractor {
        return SearchInteractorImpl(getTracksRepository())
    }

    private fun getPreferencesRepository(): PreferencesRepository {
        return PreferencesRepositoryImpl(
            SharedPrefsClient(
                applicationContext.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE)
            )
        )
    }

    fun providePreferencesInteractor(): PreferencesInteractor {
        return PreferencesInteractorImpl(getPreferencesRepository())
    }

    fun provideHistoryInteractor(): HistoryInteractor {
        return HistoryInteractorImpl(getPreferencesRepository())
    }

    private fun getPlayerRepository(): PlayerRepository {
        return PlayerRepositoryImpl(MediaPlayer())
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

    fun provideSettingsInteractor(): SettingsInteractor {
        return SettingsInteractorImpl(applicationContext)
    }
}