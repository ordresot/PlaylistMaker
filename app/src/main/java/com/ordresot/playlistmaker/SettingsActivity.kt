package com.ordresot.playlistmaker

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.FrameLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.textview.MaterialTextView

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_settings)

        val toolbar = findViewById<MaterialToolbar>(R.id.activity_settings_toolbar)
        toolbar.setNavigationOnClickListener{
            finish()
        }

        val shareElement = findViewById<MaterialTextView>(R.id.shareElement)
        shareElement.setOnClickListener {
            val shareIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, getString(R.string.link_to_android_developer_course))
                type = "text/plain"
            }
            startActivity(shareIntent)
        }

        val supportElement = findViewById<MaterialTextView>(R.id.supportElement)
        supportElement.setOnClickListener{
            val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:")
                putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.my_email)))
                putExtra(Intent.EXTRA_SUBJECT, getString(R.string.email_subject))
                putExtra(Intent.EXTRA_TEXT, getString(R.string.email_text))
            }
            startActivity(emailIntent)
        }

        val userAgreement = findViewById<MaterialTextView>(R.id.userAgreement)
        userAgreement.setOnClickListener{
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.link_to_user_agreement)))
            startActivity(browserIntent)
        }
    }
}