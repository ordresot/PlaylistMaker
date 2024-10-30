 package com.ordresot.playlistmaker

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val buttonSearch = findViewById<Button>(R.id.button_search)
        val buttonLibrary = findViewById<Button>(R.id.button_library)
        val buttonSettings = findViewById<Button>(R.id.button_settings)

        val buttonSearchClickListener: View.OnClickListener = object : View.OnClickListener {
            override fun onClick(v: View?) {
                Toast.makeText(this@MainActivity, "Ищу композицию...", Toast.LENGTH_SHORT).show()
            }
        }
        buttonSearch.setOnClickListener(buttonSearchClickListener)

        buttonLibrary.setOnClickListener{
            Toast.makeText(this@MainActivity, "Открываю медиатеку", Toast.LENGTH_SHORT).show()
        }

        buttonSettings.setOnClickListener{
            Toast.makeText(this@MainActivity, "Открываю настройки", Toast.LENGTH_SHORT).show()
        }
    }
}