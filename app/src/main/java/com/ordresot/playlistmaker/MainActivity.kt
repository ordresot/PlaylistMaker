 package com.ordresot.playlistmaker

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.ordresot.playlistmaker.databinding.ActivityMainBinding

 class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

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
}