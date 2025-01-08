package com.ordresot.playlistmaker

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.ordresot.playlistmaker.databinding.ActivityLibraryBinding

class LibraryActivity : AppCompatActivity() {
    private val binding: ActivityLibraryBinding by lazy { ActivityLibraryBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
    }
}