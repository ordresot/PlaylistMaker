package com.ordresot.playlistmaker

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.gson.Gson
import com.ordresot.playlistmaker.databinding.ActivityPlayerBinding

class PlayerActivity : AppCompatActivity() {

    private val binding: ActivityPlayerBinding by lazy { ActivityPlayerBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        val track = Gson().fromJson(intent.getStringExtra(TRACK_EXTRA), Track::class.java)

        binding.activityPlayerToolbar.setNavigationOnClickListener{
            finish()
        }

        Glide.with(applicationContext)
            .load(track.getCoverArtwork())
            .placeholder(R.drawable.track_image_large_placeholder)
            .fitCenter()
            .transform(RoundedCorners(8))
            .into(binding.trackImage)

        binding.trackName.text = track.getTrackName()

        binding.trackArtist.text = track.getTrackArtist()

        binding.trackTimeValue.text = track.getTrackTime()

        binding.trackAlbumValue.text = track.getTrackCollectionName()
        binding.trackAlbum.isVisible = !track.getTrackCollectionName().isNullOrEmpty()
        binding.trackAlbumValue.isVisible = !track.getTrackCollectionName().isNullOrEmpty()

        binding.trackDateValue.text = track.getTrackReleaseDate().take(4)

        binding.trackGenreValue.text = track.getTrackGenreName()

        binding.trackCountryValue.text = track.getTrackCountry()
    }
}