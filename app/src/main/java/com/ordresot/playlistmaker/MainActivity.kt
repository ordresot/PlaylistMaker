 package com.ordresot.playlistmaker

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private var buttonSearch: Button? = null
    private var buttonLibrary: Button? = null
    private var buttonSettings: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        buttonSearch = findViewById(R.id.button_search)
        buttonLibrary = findViewById(R.id.button_library)
        buttonSettings = findViewById(R.id.button_settings)

        buttonSearch!!.setOnClickListener{
            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)
        }

        buttonLibrary!!.setOnClickListener{
            val intent = Intent(this, LibraryActivity::class.java)
            startActivity(intent)
        }

        buttonSettings!!.setOnClickListener{
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }
    }
}