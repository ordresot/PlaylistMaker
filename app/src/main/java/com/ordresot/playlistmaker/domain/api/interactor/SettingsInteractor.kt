package com.ordresot.playlistmaker.domain.api.interactor

import android.content.Intent

interface SettingsInteractor {
    fun share(consumer: SettingsConsumer)
    fun support(consumer: SettingsConsumer)
    fun userAgreement(consumer: SettingsConsumer)

    interface SettingsConsumer {
        fun consume(intent: Intent)
    }
}