package com.ordresot.playlistmaker.ui.settings

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.ordresot.playlistmaker.util.Creator
import com.ordresot.playlistmaker.databinding.ActivitySettingsBinding
import com.ordresot.playlistmaker.domain.api.interactor.PreferencesInteractor
import com.ordresot.playlistmaker.domain.api.interactor.SettingsInteractor
import com.ordresot.playlistmaker.domain.api.use_case.SetThemeModeUseCase

class SettingsActivity : AppCompatActivity() {
    private val binding: ActivitySettingsBinding by lazy { ActivitySettingsBinding.inflate(layoutInflater) }
    private lateinit var prefsInteractor: PreferencesInteractor
    private lateinit var setThemeModeUC: SetThemeModeUseCase
    private lateinit var settingsInteractor: SettingsInteractor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        prefsInteractor = Creator.providePreferencesInteractor()
        setThemeModeUC = Creator.provideSetThemeModeUseCase()
        settingsInteractor = Creator.provideSettingsInteractor()

        binding.activitySettingsToolbar.setNavigationOnClickListener{ finish() }

        binding.shareElement.setOnClickListener { share() }

        binding.supportElement.setOnClickListener{ support() }

        binding.userAgreement.setOnClickListener{ userAgreement() }

        setDarkThemeSwitch()

        binding.darkThemeSwitch.setOnCheckedChangeListener{ switcher, checked ->
            prefsInteractor.setDarkTheme(checked)
            setThemeModeUC.setThemeMode(checked)
        }
    }

    private fun setDarkThemeSwitch(){
        prefsInteractor.getDarkTheme(
            object : PreferencesInteractor.PreferencesConsumer {
                override fun consume(value: Boolean) {
                    binding.darkThemeSwitch.isChecked = value
                }
            }
        )
    }

    private fun share() {
        settingsInteractor.share(
            object : SettingsInteractor.SettingsConsumer {
                override fun consume(intent: Intent) {
                    startActivity(intent)
                }
            }
        )
    }

    private fun support() {
        settingsInteractor.support(
            object : SettingsInteractor.SettingsConsumer {
                override fun consume(intent: Intent) {
                    startActivity(intent)
                }
            }
        )
    }

    private fun userAgreement() {
        settingsInteractor.userAgreement(
            object : SettingsInteractor.SettingsConsumer {
                override fun consume(intent: Intent) {
                    startActivity(intent)
                }
            }
        )
    }
}