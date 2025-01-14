package com.ordresot.playlistmaker

import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.ordresot.playlistmaker.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {
    private val binding: ActivitySettingsBinding by lazy { ActivitySettingsBinding.inflate(layoutInflater) }
    private val themePreferences: SharedPreferences by lazy { getSharedPreferences(THEME_PREFERENCES, MODE_PRIVATE) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        binding.activitySettingsToolbar.setNavigationOnClickListener{
            finish()
        }

        binding.shareElement.setOnClickListener {
            val shareIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, getString(R.string.link_to_android_developer_course))
                type = "text/plain"
            }
            startActivity(shareIntent)
        }

        binding.supportElement.setOnClickListener{
            val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:")
                putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.my_email)))
                putExtra(Intent.EXTRA_SUBJECT, getString(R.string.email_subject))
                putExtra(Intent.EXTRA_TEXT, getString(R.string.email_text))
            }
            startActivity(emailIntent)
        }

        binding.userAgreement.setOnClickListener{
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.link_to_user_agreement)))
            startActivity(browserIntent)
        }

        binding.darkThemeSwitch.isChecked = themePreferences.getBoolean(DARK_THEME, false)

        binding.darkThemeSwitch.setOnCheckedChangeListener{ switcher, checked ->
            (applicationContext as App).switchTheme(checked)
        }
    }
}