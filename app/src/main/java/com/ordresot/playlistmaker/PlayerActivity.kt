package com.ordresot.playlistmaker

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
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

        with(binding){
            activityPlayerToolbar.setNavigationOnClickListener{
                finish()
            }

            Glide.with(applicationContext)
                .load(track.getCoverArtwork())
                .placeholder(R.drawable.track_image_large_placeholder)
                .fitCenter()
                .transform(RoundedCorners(8))
                .into(trackImage)

            trackName.text = track.getTrackName()

            trackArtist.text = track.getTrackArtist()

            trackTimeValue.text = track.getTrackTime()

            trackAlbumValue.text = track.getTrackCollectionName()
            trackAlbum.isVisible = !track.getTrackCollectionName().isNullOrEmpty()
            trackAlbumValue.isVisible = !track.getTrackCollectionName().isNullOrEmpty()

            trackDateValue.text = track.getTrackReleaseDate().take(4)

            trackGenreValue.text = track.getTrackGenreName()

            trackCountryValue.text = track.getTrackCountry()
        }
    }
}