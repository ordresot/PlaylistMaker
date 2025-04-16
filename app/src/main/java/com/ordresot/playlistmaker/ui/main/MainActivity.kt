 package com.ordresot.playlistmaker.ui.main

import android.app.UiModeManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.ordresot.playlistmaker.Creator
import com.ordresot.playlistmaker.databinding.ActivityMainBinding
import com.ordresot.playlistmaker.domain.api.interactor.PreferencesInteractor
import com.ordresot.playlistmaker.domain.api.use_case.SetThemeModeUseCase
import com.ordresot.playlistmaker.ui.library.LibraryActivity
import com.ordresot.playlistmaker.ui.search.SearchActivity
import com.ordresot.playlistmaker.ui.settings.SettingsActivity

 class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private lateinit var prefsInteractor: PreferencesInteractor
    private lateinit var setThemeModeUC: SetThemeModeUseCase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        prefsInteractor = Creator.providePreferencesInteractor(this)
        setThemeModeUC = Creator.provideSetThemeModeUseCase()
        firstRunChecker()

        binding.buttonSearch.setOnClickListener{
            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)
        }

        binding.buttonLibrary.setOnClickListener{
            val intent = Intent(this, LibraryActivity::class.java)
            startActivity(intent)
        }

        binding.buttonSettings.setOnClickListener{
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }
    }

     private fun firstRunChecker(){
         prefsInteractor.getFirstRun(
             object : PreferencesInteractor.PreferencesConsumer {
                 override fun consume(value: Boolean) {
                     if (value){
                         val uiModeManager = getSystemService(Context.UI_MODE_SERVICE) as UiModeManager
                         prefsInteractor.setFirstRun(false)
                         prefsInteractor.setDarkTheme(uiModeManager.nightMode == UiModeManager.MODE_NIGHT_YES)
                     }
                     setPreferenceTheme()
                 }
             }
         )
     }

     private fun setPreferenceTheme(){
         prefsInteractor.getDarkTheme(
             object : PreferencesInteractor.PreferencesConsumer {
                 override fun consume(value: Boolean) {
                     setThemeModeUC.setThemeMode(value)
                 }
             }
         )
     }
}