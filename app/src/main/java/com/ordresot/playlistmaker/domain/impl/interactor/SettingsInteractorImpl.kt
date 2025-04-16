package com.ordresot.playlistmaker.domain.impl.interactor

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.ordresot.playlistmaker.R
import com.ordresot.playlistmaker.domain.api.interactor.SettingsInteractor

class SettingsInteractorImpl(private val context: Context): SettingsInteractor {
    override fun share(consumer: SettingsInteractor.SettingsConsumer) {
        consumer.consume(
            Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, context.getString(R.string.link_to_android_developer_course))
                type = "text/plain"
            }
        )
    }

    override fun support(consumer: SettingsInteractor.SettingsConsumer) {
        consumer.consume(
            Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:")
                putExtra(Intent.EXTRA_EMAIL, arrayOf(context.getString(R.string.my_email)))
                putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.email_subject))
                putExtra(Intent.EXTRA_TEXT, context.getString(R.string.email_text))
            }
        )
    }

    override fun userAgreement(consumer: SettingsInteractor.SettingsConsumer) {
        consumer.consume(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse(
                    context.getString(R.string.link_to_user_agreement)
                )
            )
        )
    }
}