package com.ordresot.playlistmaker

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.gson.Gson
import com.ordresot.playlistmaker.databinding.ActivityPlayerBinding
import java.text.SimpleDateFormat
import java.util.Locale

const val DELAY = 500L

class PlayerActivity : AppCompatActivity() {

    private val binding: ActivityPlayerBinding by lazy { ActivityPlayerBinding.inflate(layoutInflater) }
    private val timerHandler: Handler by lazy { Handler(Looper.getMainLooper()) }
    private val track: Track by lazy { Gson().fromJson(intent.getStringExtra(TRACK_EXTRA), Track::class.java) }
    private val mediaPlayer: Player by lazy { Player(track.getPreviewUrl()) }
    private val timerRunnable = Runnable { createUpdateTimerTask() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        mediaPlayer.getPlayerState().observe(this){ stateValue ->
            when(stateValue){
                PlayerState.PLAYING -> {
                    binding.playButton.setImageResource(R.drawable.ic_button_pause)
                    timerHandler.post(
                        timerRunnable
                    )
                }
                PlayerState.PAUSED -> {
                    binding.playButton.setImageResource(R.drawable.ic_button_play)
                    timerHandler.removeCallbacks(
                        timerRunnable
                    )
                }
                PlayerState.PREPARED -> {
                    binding.currentPlaytime.text = resources.getString(R.string.default_track_current_time)
                }
                PlayerState.DEFAULT -> {}
            }
        }

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
            trackAlbumValue.isVisible = !track.getTrackCollectionName().isNullOrEmpty()

            trackAlbum.isVisible = !track.getTrackCollectionName().isNullOrEmpty()

            trackDateValue.text = track.getTrackReleaseDate().take(4)

            trackGenreValue.text = track.getTrackGenreName()

            trackCountryValue.text = track.getTrackCountry()

            playButton.setOnClickListener{
                mediaPlayer.playbackControl()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        mediaPlayer.pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.stopPlayer()
    }

    private fun createUpdateTimerTask() {
        binding.currentPlaytime.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(mediaPlayer.getCurrentTrackTime())
        timerHandler.postDelayed(timerRunnable, DELAY)
    }
}